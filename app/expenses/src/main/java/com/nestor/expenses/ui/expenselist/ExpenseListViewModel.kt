package com.nestor.expenses.ui.expenselist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nestor.common.data.auth.AuthRepository
import com.nestor.common.data.currency.CurrencyRepository
import com.nestor.common.data.monthandyear.MonthAndYear
import com.nestor.database.data.expense.ExpenseEntity
import com.nestor.expenses.data.ExpenseRepository
import com.nestor.uikit.util.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

const val PAGE_SIZE = 20
private const val TAG = "ExpenseListViewModel"

@HiltViewModel
class ExpenseListViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val currencyRepository: CurrencyRepository,
    private val coroutineDispatcher: CoroutineContextProvider,
    private val authRepository: AuthRepository,
    private val monthYear: MonthAndYear,
) : ViewModel() {
    private val _userCurrencySymbol = MutableStateFlow("$")
    val userCurrencySymbol = _userCurrencySymbol
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> =
        _isLoading.stateIn(viewModelScope, SharingStarted.Lazily, false)
    val expenseItems: StateFlow<List<ExpenseEntity>> =
        authRepository.userDetails()
            .map { it.body }
            .filterNotNull()
            .transform {
                emitAll(
                    expenseRepository.getExpenses(
                        month = monthYear.month,
                        year = monthYear.year,
                        userUid = it.uuid,
                        currencyCode = it.currencyCode,
                    )
                )
            }
            .onEach { if (it.isEmpty()) fetchMoreItems() }
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        fetchUserCurrencyInfo()
    }

    private fun fetchMoreItems() {
        synchronized(this) {
            Log.i(TAG, "fetchMoreItems: ")
            if (_isLoading.value) {
                Log.i(TAG, "fetchMoreItems: Already loading...")
                return
            }
            viewModelScope.launch(coroutineDispatcher.io()) {
                _isLoading.update { true }
                val userDetails =
                    authRepository.userDetails().map { it.body }.filterNotNull().take(1).last()
                val lastExpenseItem = expenseItems.map { it.lastOrNull() }.take(1).last()
                expenseRepository.fetchMoreExpenses(
                    month = monthYear.month,
                    year = monthYear.year,
                    userUid = userDetails.uuid,
                    currencyCode = userDetails.currencyCode,
                    pageSize = PAGE_SIZE,
                    cursor = lastExpenseItem?.cursor ?: 0
                )
                _isLoading.update { false }
            }
        }
    }

    private fun fetchUserCurrencyInfo() {
        viewModelScope.launch(coroutineDispatcher.io()) {
            authRepository.userDetails()
                .map { it.body?.currencyCode }
                .filterNotNull()
                .collect { code ->
                    currencyRepository.fetchCurrencyByCode(code)
                        .map { it.body }
                        .filterNotNull()
                        .collect { currency ->
                            _userCurrencySymbol.update { currency.symbol }
                        }
                }
        }
    }

    fun onScrollEndReached() {
        synchronized(this) {
            Log.i(TAG, "onScrollEndReached: ")
            if (_isLoading.value) {
                Log.i(TAG, "onScrollEndReached: Already loading...")
                return
            }
            fetchMoreItems()
        }
    }
}

