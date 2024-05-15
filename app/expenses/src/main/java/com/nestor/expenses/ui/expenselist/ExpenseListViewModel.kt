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
import kotlinx.coroutines.flow.combine
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
    private val _currencyUSDExchangeRate = MutableStateFlow(1.0)
    private val _userCurrencySymbol = MutableStateFlow("$")
    val userCurrencySymbol = _userCurrencySymbol
    val expenseItems = combine(
        expenseRepository.getExpensesList(),
        _currencyUSDExchangeRate
    ) { expenseList: ResponseWrapper<ExpenseList>, exchangeRate: Double ->
        expenseList.mapBody { list ->
            list.copy(
                items = list.items.map { item -> item.copy(usdValue = item.amount) }
            )
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, ResponseWrapper.loading())

    init {
        fetchExpenses()
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
                            _currencyUSDExchangeRate.update { currency.usdRate }
                        }
                }
        }
    }

    fun fetchExpenses(lastCursor: Int? = null) {
        viewModelScope.launch(coroutineDispatcher.io()) {
            val monthYearValue = monthYear.value
            expenseRepository.getExpenses(
                month = monthYearValue.second,
                year = monthYearValue.first,
                pageSize = PAGE_SIZE,
                cursor = lastCursor
            )
        }
    }
}

