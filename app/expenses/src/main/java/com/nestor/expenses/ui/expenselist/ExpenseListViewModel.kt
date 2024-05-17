package com.nestor.expenses.ui.expenselist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nestor.common.data.auth.AuthRepository
import com.nestor.common.data.currency.CurrencyRepository
import com.nestor.database.data.expense.ExpenseEntity
import com.nestor.expenses.data.ExpenseRepository
import com.nestor.uikit.util.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
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
    private val authRepository: AuthRepository
) : ViewModel() {
    private val monthYear: MutableStateFlow<Pair<Int, Int>> = MutableStateFlow(Pair(2024, 5))
    private val _userCurrencySymbol = MutableStateFlow("$")
    val userCurrencySymbol = _userCurrencySymbol
    val expenseItems: StateFlow<List<ExpenseEntity>> =
        monthYear
            .combineTransform(authRepository.userDetails()
                .map { it.body }
                .filterNotNull()) { monthYear, user ->
                emitAll(
                    expenseRepository.getExpenses(
                        year = monthYear.first,
                        month = monthYear.second,
                        userUid = user.uuid,
                        currencyCode = user.currencyCode
                    )
                )
            }
            .onEach { if (it.isEmpty()) this.fetchMoreItems() }
            .stateIn(
                viewModelScope,
                SharingStarted.Lazily,
                emptyList()
            )

    init {
        fetchUserCurrencyInfo()
    }

    private fun fetchMoreItems() {
        Log.i(TAG, "fetchMoreItems: ")
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
        this.fetchMoreItems()
    }
}

