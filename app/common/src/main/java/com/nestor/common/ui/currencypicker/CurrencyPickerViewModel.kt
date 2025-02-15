package com.nestor.common.ui.currencypicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nestor.common.data.auth.AuthRepository
import com.nestor.common.data.currency.CurrencyRepository
import com.nestor.database.data.currency.CurrencyEntity
import com.nestor.uikit.input.FormFieldData
import com.nestor.uikit.list.SYListItemData
import com.nestor.uikit.util.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyPickerViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository,
    private val authRepository: AuthRepository,
    coroutineDispatcher: CoroutineContextProvider,
) : ViewModel() {
    private val _filterText = MutableStateFlow(FormFieldData(""))
    val filterText = _filterText.asStateFlow()
    private val _filteredCurrencies = MutableStateFlow<List<CurrencyEntity>>(emptyList())
    private val _originalCurrencies = MutableStateFlow<List<CurrencyEntity>>(emptyList())
    val filteredCurrencies = _filteredCurrencies.asStateFlow()
    private val _selectedCurrency = MutableStateFlow<CurrencyEntity?>(null)
    val selectedCurrency = _selectedCurrency.asStateFlow()

    init {
        viewModelScope.launch(coroutineDispatcher.io()) {
            currencyRepository
                .fetchCurrencies()
                .collect { currenciesResponse ->
                    currenciesResponse.body?.let { currencies ->
                        _originalCurrencies.update { currencies }
                        _filteredCurrencies.update { currencies }
                    }
                }
        }
        viewModelScope.launch(coroutineDispatcher.io()) {
            authRepository.userDetails()
                .map { it.body?.currencyCode }
                .filterNotNull()
                .collect { code ->
                    currencyRepository.fetchCurrencyByCode(code)
                        .map { it.body }
                        .collect { currency ->
                            _selectedCurrency.update { currency }
                        }
                }
        }
    }

    fun onFilterTextChange(query: String) {
        _filterText.update { it.copy(query) }
        if (query.isEmpty()) {
            _filteredCurrencies.update { _originalCurrencies.value }
        } else {
            _filteredCurrencies.update {
                _originalCurrencies.value.filter { currency ->
                    currency.code.contains(
                        query,
                        ignoreCase = true
                    ) || currency.name.contains(query, ignoreCase = true)
                }
            }
        }
        reorderCurrencies()
    }

    fun setInitialCurrencyValue(initialValue: CurrencyEntity?) {
        _selectedCurrency.update { initialValue }
        if (initialValue != null) {
            reorderCurrencies()
        }
    }

    /**
     * Selected currency should be always on position 0 of the list
     */
    private fun reorderCurrencies() {
        _filteredCurrencies.update {
            val selectedCurrency = _selectedCurrency.value
            val filteredCurrencies = it.toMutableList()
            if (selectedCurrency != null) {
                filteredCurrencies.remove(selectedCurrency)
                filteredCurrencies.add(0, selectedCurrency)
            }
            filteredCurrencies
        }
    }

    fun onCurrencySelected(currencyCode: String) {
        _selectedCurrency.update {
            _filteredCurrencies.value.find { it.code == currencyCode }
        }
    }
}