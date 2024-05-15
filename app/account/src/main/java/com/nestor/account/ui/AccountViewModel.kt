package com.nestor.account.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nestor.common.data.auth.AuthRepository
import com.nestor.common.data.currency.CurrencyRepository
import com.nestor.dashboard.data.DashboardRepository
import com.nestor.database.data.currency.CurrencyEntity
import com.nestor.uikit.util.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val currencyRepository: CurrencyRepository,
    private val coroutineContextProvider: CoroutineContextProvider,
    private val dashboardRepository: DashboardRepository
) : ViewModel() {
    private val _currencyPickerVisible = MutableStateFlow(false)
    val currencyPickerVisible = _currencyPickerVisible
    private val _selectedCurrency = MutableStateFlow<CurrencyEntity?>(null)
    val selectedCurrency = _selectedCurrency

    init {
        viewModelScope.launch(coroutineContextProvider.io()) {
            authRepository.userDetails().collect { userDetails ->
                userDetails.body?.currencyCode?.let { currencyCode ->
                    currencyRepository.fetchCurrencyByCode(currencyCode).collect { currency ->
                        _selectedCurrency.value = currency.body
                    }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch(coroutineContextProvider.io()) {
            authRepository.logout()
        }
    }

    fun onCurrencySelected(currencyCode: String) {
        viewModelScope.launch(coroutineContextProvider.io()) {
            currencyRepository.fetchCurrencyByCode(currencyCode)
                .collect {
                    it.body?.let { currency ->
                        currencyRepository.updateUserCurrency(currency)
                        dashboardRepository.refreshDashboardData()
                    }
                }
        }
    }

    fun onCurrencyClick() {
        _currencyPickerVisible.value = true
    }

    fun onCurrencyPickerDismiss() {
        _currencyPickerVisible.value = false
    }
}