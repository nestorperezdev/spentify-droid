package com.nestor.dashboard.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nestor.common.data.auth.AuthRepository
import com.nestor.common.data.currency.CurrencyRepository
import com.nestor.dashboard.data.DashboardRepository
import com.nestor.database.data.currency.CurrencyEntity
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
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class DashboardViewModel @Inject constructor(
    dashboardRepository: DashboardRepository,
    coroutineContextProvider: CoroutineContextProvider,
    currencyRepository: CurrencyRepository,
    authRepository: AuthRepository
) : ViewModel() {
    val userDetails: StateFlow<com.nestor.schema.utils.ResponseWrapper<UserDetails>> =
        dashboardRepository.fetchDashboardInfo().map {
            it.mapBody { UserDetails(it.userName, it.dailyPhrase) }
        }.stateIn(viewModelScope, SharingStarted.Lazily, com.nestor.schema.utils.ResponseWrapper.loading())

    val summary: StateFlow<com.nestor.schema.utils.ResponseWrapper<DailySummary>> =
        authRepository.userDetails()
            .map { it.body }
            .filterNotNull()
            .transformLatest { user ->
                emitAll(currencyRepository.fetchCurrencyByCode(user.currencyCode))
            }
            .combine(dashboardRepository.fetchDashboardInfo()) { currencyWrapper, summaryWrapper ->
                summaryWrapper.combineTransform(currencyWrapper) { summary, currency ->
                    DailySummary(
                        totalExpenses = summary.totalExpenses,
                        minimalExpense = summary.minimalExpense,
                        dailyAverageExpense = summary.dailyAverageExpense,
                        maximalExpense = summary.maximalExpense,
                        userCurrency = UserCurrency(
                            usdValue = currency.usdRate,
                            symbol = currency.symbol,
                            code = currency.code,
                        )
                    )
                }
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, com.nestor.schema.utils.ResponseWrapper.loading())

    init {
        viewModelScope.launch(coroutineContextProvider.io()) {
            userDetails.collect()
        }
    }
}