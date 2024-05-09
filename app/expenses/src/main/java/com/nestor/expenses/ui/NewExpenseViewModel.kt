package com.nestor.expenses.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nestor.common.data.CurrencyRepository
import com.nestor.dashboard.data.DashboardRepository
import com.nestor.expenses.data.ExpenseRepository
import com.nestor.schema.type.Currency
import com.nestor.schema.type.ExpenseInput
import com.nestor.schema.utils.ResponseWrapper
import com.nestor.uikit.input.FormFieldData
import com.nestor.uikit.util.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NewExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val currencyRepository: CurrencyRepository,
    private val coroutineDispatcher: CoroutineContextProvider,
    private val dashRepo: DashboardRepository
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

    private fun isValidInput(input: String): Boolean {
        return inputRegex.matches(input)
    }

    fun onDescriptionChanged(text: String) {
        if (text.length < 255) {
            _description.update { it.copy(text) }
        }
    }

    fun onSave() {
        val amount = _amount.value.value.toDouble()
        viewModelScope.launch(coroutineDispatcher.io()) {
            //  todo: use a real exchange rate here!!
            val expenseInput = ExpenseInput(
                currentExchangeId = "3bfa95b0-33c6-4ddd-bd24-4e6c84e8cca0",
                description = _description.value.value,
                value = amount
            )
            _loadingState.update { true }
            val result = expenseRepository.createExpense(expenseInput)
            if (result.isSuccessful()) {
                launch { dashRepo.refreshDashboardData() }
                _dismissDialog.update { true }
            } else {
                _loadingState.update { false }
            }
        }
    }

    fun onAmountChanged(text: String) {
        if (this.isValidInput(text)) {
            _amount.update { it.copy(text) }
        }
    }
}