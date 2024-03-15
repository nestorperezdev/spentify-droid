package com.nestor.dashboard.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nestor.auth.data.AuthRepository
import com.nestor.common.data.CurrencyRepository
import com.nestor.dashboard.data.DashboardRepository
import com.nestor.schema.utils.ResponseWrapper
import com.nestor.uikit.util.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    val dashboardInfo: StateFlow<DashboardUiState> = _dashboardUiState

    init {
        viewModelScope.launch(coroutineContextProvider.io()) {
            dashboardRepository.fetchDashboardInfo()
                .combine(
                    authRepository.userDetails().filterNotNull()
                ) { dashboardInfoResponse, userDetailsResponse ->
                    if (dashboardInfoResponse.isLoading) {
                        _dashboardUiState.update { it.copy(summary = ResponseWrapper.loading()) }
                    }
                    if (userDetailsResponse.isLoading) {
                        _dashboardUiState.update { it.copy(userDetails = ResponseWrapper.loading()) }
                    }
                    dashboardInfoResponse.body?.let { dashboardInfo ->
                        _dashboardUiState.update {
                            it.copy(
                                userDetails = ResponseWrapper.success(
                                    UserDetails(
                                        userName = dashboardInfo.userName,
                                        dailyPhrase = dashboardInfo.dailyPhrase
                                    )
                                )
                            )
                        }
                        userDetailsResponse.body?.let { userDetails ->
                            currencyRepository
                                .fetchCurrencyByCode(userDetails.currencyCode)
                                .collect { currencyResponse ->
                                    if (currencyResponse.isLoading) {
                                        _dashboardUiState.update { it.copy(userCurrency = ResponseWrapper.loading()) }
                                    }
                                    currencyResponse.body?.let { currency ->
                                        _dashboardUiState.update {
                                            it.copy(
                                                summary = ResponseWrapper.success(
                                                    DailySummary(
                                                        totalExpenses = dashboardInfo.totalExpenses * currency.usdRate,
                                                        minimalExpense = dashboardInfo.minimalExpense * currency.usdRate,
                                                        dailyAverageExpense = dashboardInfo.dailyAverageExpense * currency.usdRate,
                                                        maximalExpense = dashboardInfo.maximalExpense * currency.usdRate
                                                    )
                                                )
                                            )
                                        }
                                    }
                                }
                        }
                    }
                }
                .collect()
        }
    }

    fun onDifferentCurrencySelect() {
        viewModelScope.launch(coroutineContextProvider.io()) {
            currencyRepository.fetchCurrencies()
                .map { it.body }
                .filterNotNull()
                .collect { currencies ->
                    val currentCurrency = _dashboardUiState.value.userCurrency.body?.code ?: "USD"
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