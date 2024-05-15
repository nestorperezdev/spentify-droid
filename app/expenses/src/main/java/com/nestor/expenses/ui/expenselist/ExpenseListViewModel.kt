package com.nestor.expenses.ui.expenselist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nestor.common.data.auth.AuthRepository
import com.nestor.common.data.currency.CurrencyRepository
import com.nestor.expenses.data.ExpenseRepository
import com.nestor.schema.utils.ResponseWrapper
import com.nestor.schema.utils.mapBody
import com.nestor.uikit.util.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

const val PAGE_SIZE = 20

@HiltViewModel
class ExpenseListViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val currencyRepository: CurrencyRepository,
    private val coroutineDispatcher: CoroutineContextProvider,
    private val authRepository: AuthRepository
) : ViewModel() {
    var monthYear: MutableStateFlow<Pair<Int, Int>> = MutableStateFlow(Pair(2024, 5))
    private val _userCurrencySymbol = MutableStateFlow("$")
    val userCurrencySymbol = _userCurrencySymbol
    val expenseItems: StateFlow<ResponseWrapper<ExpenseList>> =
        authRepository.userDetails()
            .map { it.body }
            .filterNotNull()
            .combineTransform(monthYear) { user, monthYear ->
                emitAll(
                    expenseRepository.getExpenses(
                        month = monthYear.second,
                        year = monthYear.first,
                        pageSize = PAGE_SIZE,
                        userUid = user.uuid,
                        cursor = null
                    )
                )
            }.map {
                it.mapBody { list ->
                    ExpenseList(
                        totalItems = 20,
                        items = list,
                        endReached = false,
                    )
                }
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, ResponseWrapper.loading())

    init {
        fetchUserCurrencyInfo()
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
}

