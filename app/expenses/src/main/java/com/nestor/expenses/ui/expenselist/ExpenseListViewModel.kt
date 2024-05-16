package com.nestor.expenses.ui.expenselist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nestor.common.data.auth.AuthRepository
import com.nestor.common.data.currency.CurrencyRepository
import com.nestor.database.data.expense.ExpenseEntity
import com.nestor.expenses.data.ExpenseRepository
import com.nestor.schema.ExpensesListQuery
import com.nestor.schema.type.PaginationData
import com.nestor.schema.utils.ResponseWrapper
import com.nestor.schema.utils.mapBody
import com.nestor.uikit.util.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
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
    private val _currentPage = MutableStateFlow(0)
    val paginationResult: StateFlow<ResponseWrapper<ExpensesListQuery.Pagination>> = combineTransform(
        _currentPage.filter { it > 0 },
        monthYear,
        authRepository.userDetails().map { it.body }.filterNotNull()
    ) { page, monthYear, user ->
        emitAll(
            expenseRepository.fetchMoreExpenses(
                page = page,
                pageSize = PAGE_SIZE,
                userUid = user.uuid,
                year = monthYear.first,
                month = monthYear.second
            )
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        ResponseWrapper.loading(null)
    )
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
                        userUid = user.uuid
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
        val page = this.calculateCurrentPage()
        if (page > (paginationResult.value.body?.paginationFragment?.totalPages ?: 0)) {
            Log.d(TAG, "fetchMoreItems: No more pages to fetch")
            return
        }
        Log.i(TAG, "fetchMoreItems: Fetching page $page")
        _currentPage.update { it + 1 }
    }

    private fun calculateCurrentPage(): Int {
        val currentSize = expenseItems.value.size
        return currentSize / PAGE_SIZE
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
        if (paginationResult.value.isLoading) {
            Log.d(TAG, "onScrollEndReached: Already loading")
            return
        }
        this.fetchMoreItems()
    }
}

