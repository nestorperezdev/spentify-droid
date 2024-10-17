package com.nestor.expenses.ui.expenselist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nestor.category.data.subcategory.SubcategoryRepository
import com.nestor.common.data.auth.AuthRepository
import com.nestor.common.data.currency.CurrencyRepository
import com.nestor.common.data.monthandyear.MonthAndYear
import com.nestor.dashboard.data.DashboardRepository
import com.nestor.database.data.expense.ExpenseEntity
import com.nestor.database.data.expensewithcategory.ExpenseWithCategoryEntity
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
import java.util.Date
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
    private val dashboardRepository: DashboardRepository,
    private val subcategoryRepository: SubcategoryRepository
) : ViewModel() {
    private val _userCurrencySymbol = MutableStateFlow("$")
    val userCurrencySymbol = _userCurrencySymbol
    private val _isLoading = MutableStateFlow(false)
    private val _isEndOfList = MutableStateFlow(false)
    val isEndOfList: StateFlow<Boolean> = _isEndOfList.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        false
    )
    val isLoading: StateFlow<Boolean> =
        _isLoading.stateIn(viewModelScope, SharingStarted.Lazily, false)
    val expenseItems: StateFlow<List<ExpenseWithCategoryEntity>> =
        authRepository.userDetails()
            .map { it.body }
            .filterNotNull()
            .transform {
                emitAll(
                    expenseRepository.getExpenses(Date())
                )
            }
            .onEach { if (it.isEmpty()) fetchMoreItems() }
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        fetchUserCurrencyInfo()
        fetchCategoryListIfNeeded()
    }

    private fun fetchCategoryListIfNeeded() {
        viewModelScope.launch(coroutineDispatcher.io()) {
            subcategoryRepository.getSubcategories().take(1).collect {
                //  no-op
            }
        }
    }

    private fun fetchMoreItems() {
        synchronized(this) {
            Log.i(TAG, "fetchMoreItems: ")
            if (_isLoading.value) {
                Log.i(TAG, "fetchMoreItems: Already loading...")
                return
            }
            if (_isEndOfList.value) {
                Log.i(TAG, "fetchMoreItems: No more items to fetch")
                return
            }
            viewModelScope.launch(coroutineDispatcher.io()) {
                _isLoading.update { true }
                val userDetails =
                    authRepository.userDetails().map { it.body }.filterNotNull().take(1).last()
                val lastExpenseItem = expenseItems.map { it.lastOrNull() }.take(1).last()
                val itemsFetched = expenseRepository.fetchMoreExpenses(
                    month = monthYear.month,
                    year = monthYear.year,
                    userUid = userDetails.uuid,
                    currencyCode = userDetails.currencyCode,
                    pageSize = PAGE_SIZE,
                    cursor = lastExpenseItem?.expense?.cursor ?: 0
                )
                if (itemsFetched < PAGE_SIZE) {
                    _isEndOfList.update { true }
                }
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
            if (_isEndOfList.value) {
                Log.i(TAG, "onScrollEndReached: No more items to fetch")
                return
            }
            fetchMoreItems()
        }
    }

    fun onDeleteExpense(expenseEntity: ExpenseEntity) {
        viewModelScope.launch(coroutineDispatcher.io()) {
            expenseRepository.deleteExpense(expenseEntity)
            //  todo: delay this call.
            dashboardRepository.refreshDashboardData()
        }
    }

    fun onEditExpense(expenseEntity: ExpenseEntity) {
        TODO("Not yet implemented")
    }
}

