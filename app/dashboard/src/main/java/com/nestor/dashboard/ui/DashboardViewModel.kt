package com.nestor.dashboard.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nestor.common.data.currency.CurrencyRepository
import com.nestor.dashboard.data.DashboardRepository
import com.nestor.schema.utils.ResponseWrapper
import com.nestor.schema.utils.combineTransform
import com.nestor.schema.utils.mapBody
import com.nestor.uikit.util.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class DashboardViewModel @Inject constructor(
    dashboardRepository: DashboardRepository,
    coroutineContextProvider: CoroutineContextProvider,
    currencyRepository: CurrencyRepository,
) : ViewModel() {
    val userDetails: StateFlow<ResponseWrapper<UserDetails>> =
        dashboardRepository.fetchDashboardInfo().map {
            it.mapBody { UserDetails(it.userName, it.dailyPhrase) }
        }.stateIn(viewModelScope, SharingStarted.Lazily, ResponseWrapper.loading())

    val summary: StateFlow<ResponseWrapper<DailySummary>> =
        dashboardRepository.fetchDashboardInfo()
            .combine(
                currencyRepository.fetchCurrentUserCurrency().filterNotNull()
            ) { dash, currency ->
                dash.combineTransform(ResponseWrapper.success(currency)) { summary, currencyWrapper ->
                    DailySummary(
                        totalExpenses = summary.totalExpenses,
                        minimalExpense = summary.minimalExpense,
                        dailyAverageExpense = summary.dailyAverageExpense,
                        maximalExpense = summary.maximalExpense,
                        userCurrency = UserCurrency(
                            usdValue = currencyWrapper.usdRate,
                            symbol = currencyWrapper.symbol,
                            code = currencyWrapper.code,
                        )
                    )
                }
            }.stateIn(viewModelScope, SharingStarted.Lazily, ResponseWrapper.loading())

    init {
        viewModelScope.launch(coroutineContextProvider.io()) {
            userDetails.collect()
        }
    }
}