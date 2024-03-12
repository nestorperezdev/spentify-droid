package com.nestor.dashboard.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nestor.auth.data.AuthRepository
import com.nestor.auth.data.model.AuthState
import com.nestor.common.data.CurrencyRepository
import com.nestor.dashboard.data.DashboardRepository
import com.nestor.uikit.util.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dashboardRepository: DashboardRepository,
    private val coroutineContextProvider: CoroutineContextProvider,
    private val currencyRepository: CurrencyRepository,
) : ViewModel() {
    private val _dashboardUiState = MutableStateFlow(DashboardUiState())
    val dashboardInfo: StateFlow<DashboardUiState> = _dashboardUiState.asStateFlow()

    init {
        viewModelScope.launch(coroutineContextProvider.io()) {
            authRepository.userDetails().collect { details ->
                if (details.isLoading) {
                    _dashboardUiState.update { it.copy(isLoading = true) }
                } else if (details.body != null) {
                    (details.body as? AuthState.Authenticated)?.details?.let { user ->
                        val currencyCode = user.currencyCode
                        combine(
                            currencyRepository
                                .fetchCurrencyByCode(currencyCode)
                                .map { it.body }
                                .filterNotNull(),
                            dashboardRepository.fetchDashboardInfo()
                                .map { it.body }
                                .filterNotNull()
                        ) { currency, dashboard ->
                            _dashboardUiState.update {
                                it.copy(
                                    userCurrency = DashboardUiState.UserCurrency(
                                        symbol = currency.symbol,
                                        usdValue = currency.usdRate,
                                        code = currency.code
                                    ),
                                    totalExpenses = dashboard.totalExpenses * currency.usdRate,
                                    dailyAverageExpense = dashboard.dailyAverageExpense * currency.usdRate,
                                    dailyPhrase = dashboard.dailyPhrase,
                                    maximalExpense = dashboard.maximalExpense * currency.usdRate,
                                    isLoading = false,
                                    userName = dashboard.userName,
                                    minimalExpense = dashboard.minimalExpense * currency.usdRate,
                                )
                            }
                        }.collect()
                    }
                }
            }
        }
    }

    fun onDifferentCurrencySelect() {
        viewModelScope.launch(coroutineContextProvider.io()) {
            currencyRepository.fetchCurrencies()
                .map { it.body }
                .filterNotNull()
                .collect { currencies ->
                    val currentCurrency = _dashboardUiState.value.userCurrency.code
                    var currencyIndex =
                        currencies.indexOfFirst { current -> current.code == currentCurrency }
                    if (currencyIndex + 1 >= currencies.size) {
                        currencyIndex = 0
                    } else {
                        currencyIndex++
                    }
                    val selectedCurrency = currencies[currencyIndex]
                    authRepository.updateUserCurrency(selectedCurrency.code)
                }
        }
    }
}