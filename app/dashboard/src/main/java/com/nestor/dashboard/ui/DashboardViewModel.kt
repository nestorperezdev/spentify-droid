package com.nestor.dashboard.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nestor.common.data.auth.AuthRepository
import com.nestor.common.data.currency.CurrencyRepository
import com.nestor.dashboard.data.DashboardRepository
import com.nestor.schema.utils.ResponseWrapper
import com.nestor.schema.utils.combineTransform
import com.nestor.schema.utils.mapBody
import com.nestor.uikit.util.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "DashboardViewModel"
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dashboardRepository: DashboardRepository,
    private val coroutineContextProvider: CoroutineContextProvider,
    private val currencyRepository: CurrencyRepository,
) : ViewModel() {
    val userDetails: StateFlow<ResponseWrapper<UserDetails>> =
        dashboardRepository.fetchDashboardInfo().map {
            it.mapBody { UserDetails(it.userName, it.dailyPhrase) }
        }.stateIn(viewModelScope, SharingStarted.Lazily, ResponseWrapper.loading())
    private val _userCurrency: StateFlow<ResponseWrapper<UserCurrency>> =
        authRepository.userDetails()
            .flatMapLatest { details ->
                withContext(coroutineContextProvider.io()) {
                    currencyRepository.fetchCurrencyByCode(details.body?.currencyCode ?: "USD")
                        .map {
                            it.combineTransform(details) { currency, _ ->
                                UserCurrency(
                                    code = currency.code,
                                    usdValue = currency.usdRate,
                                    symbol = currency.symbol
                                )
                            }
                        }
                }
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, ResponseWrapper.loading())

    val summary: StateFlow<ResponseWrapper<DailySummary>> =
        dashboardRepository.fetchDashboardInfo()
            .combine(_userCurrency) { dash, currencyResponseWrapper ->
                dash.combineTransform(currencyResponseWrapper) { summary, currency ->
                    DailySummary(
                        totalExpenses = summary.totalExpenses * currency.usdValue,
                        minimalExpense = summary.minimalExpense * currency.usdValue,
                        dailyAverageExpense = summary.dailyAverageExpense * currency.usdValue,
                        maximalExpense = summary.maximalExpense * currency.usdValue,
                        userCurrency = currency
                    )
                }
            }.stateIn(viewModelScope, SharingStarted.Lazily, ResponseWrapper.loading())

    init {
        Log.i(TAG, "init: ")
        viewModelScope.launch(coroutineContextProvider.io()) {
            userDetails.collect()
        }
    }

    override fun onCleared() {
        Log.i(TAG, "onCleared: ")
        super.onCleared()
    }
}