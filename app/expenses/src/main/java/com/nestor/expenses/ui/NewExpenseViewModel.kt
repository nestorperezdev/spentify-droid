package com.nestor.expenses.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nestor.category.data.subcategory.SubcategoryRepository
import com.nestor.common.data.auth.AuthRepository
import com.nestor.common.data.currency.CurrencyRepository
import com.nestor.dashboard.data.DashboardRepository
import com.nestor.database.data.currency.CurrencyEntity
import com.nestor.database.data.subcategory.SubcategoryWithCategories
import com.nestor.expenses.data.ExpenseRepository
import com.nestor.expenses.data.toEntity
import com.nestor.schema.type.ExpenseInput
import com.nestor.schema.utils.ResponseWrapper
import com.nestor.uikit.input.FormFieldData
import com.nestor.uikit.util.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val currencyRepository: CurrencyRepository,
    private val coroutineDispatcher: CoroutineContextProvider,
    private val subcategoryRepository: SubcategoryRepository,
    private val dashRepo: DashboardRepository,
    private val authRepository: AuthRepository
) :
    ViewModel() {
    private val inputRegex = "^\\d*\\.?\\d*\$".toRegex()
    private val _amount = MutableStateFlow(FormFieldData(""))
    val amount = _amount.asStateFlow()
    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState.asStateFlow()
    private val _dismissDialog = MutableStateFlow(false)
    val dismissDialog = _dismissDialog.asStateFlow()
    private val _description = MutableStateFlow(FormFieldData(""))
    val description = _description.asStateFlow()
    private val _selectedCurrency = MutableStateFlow<CurrencyEntity?>(null)
    val selectedCurrency = _selectedCurrency.asStateFlow()
    private val _currencyOpenState = MutableStateFlow(false)
    val currencyOpenState = _currencyOpenState.asStateFlow()
    private val _categories = MutableStateFlow(ResponseWrapper.loading<List<SubcategoryWithCategories>>())
    val categories = _categories.asStateFlow()

    init {
        viewModelScope.launch(coroutineDispatcher.io()) {
            authRepository.userDetails()
                .map { it.body?.currencyCode }
                .filterNotNull()
                .take(1)
                .collect { code ->
                    currencyRepository.fetchCurrencyByCode(code)
                        .map { it.body }
                        .filterNotNull()
                        .collect { currency ->
                            _selectedCurrency.update { currency }
                        }
                }
        }
        viewModelScope.launch(coroutineDispatcher.io()) {
            subcategoryRepository
                .getSubcategories()
                .filterNotNull()
                .take(1)
                .collect { categories ->
                    _categories.update { categories }
                }
        }
    }

    private fun isValidInput(input: String): Boolean {
        return inputRegex.matches(input)
    }

    fun onDescriptionChanged(text: String) {
        if (text.length < 255) {
            _description.update { it.copy(text) }
        }
    }

    fun onSave() {
        if (_amount.value.value.isEmpty()) return
        val amount = _amount.value.value.toDouble()
        viewModelScope.launch(coroutineDispatcher.io()) {
            _selectedCurrency.value?.let { currency ->
                val expenseInput = ExpenseInput(
                    currentExchangeId = currency.exchangeId,
                    description = _description.value.value,
                    value = amount
                )
                _loadingState.update { true }
                val result = expenseRepository.createExpense(expenseInput)
                if (result.isSuccessful()) {
                    launch { dashRepo.refreshDashboardData() }
                    launch {
                        val user =
                            authRepository.userDetails().map { it.body }.filterNotNull().take(1)
                                .last()
                        result.body?.createExpense?.expenseFragment?.toEntity(user.uuid)?.let { expense ->
                            expenseRepository.saveExpenses(expense)
                        }
                    }
                    _dismissDialog.update { true }
                } else {
                    _loadingState.update { false }
                }
            }
        }
    }

    fun onAmountChanged(text: String) {
        if (this.isValidInput(text)) {
            _amount.update { it.copy(text) }
        }
    }

    fun onCurrencySelected(currencyCode: String) {
        onCurrencyPickerDismiss()
        viewModelScope.launch(coroutineDispatcher.io()) {
            currencyRepository.fetchCurrencies().filterNotNull().take(1).collect { response ->
                response.body?.let { currencies ->
                    val currency = currencies.first { it.code == currencyCode }
                    _selectedCurrency.update { currency }
                }
            }
        }
    }

    fun onCurrencyPickerClicked() {
        _currencyOpenState.update { true }
    }

    fun onCurrencyPickerDismiss() {
        _currencyOpenState.update { false }
    }
}