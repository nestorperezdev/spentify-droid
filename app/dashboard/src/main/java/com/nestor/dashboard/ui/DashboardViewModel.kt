package com.nestor.dashboard.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nestor.auth.data.AuthRepository
import com.nestor.common.data.CurrencyRepository
import com.nestor.common.data.statusbar.StatusBarRepository
import com.nestor.dashboard.R
import com.nestor.dashboard.data.DashboardRepository
import com.nestor.schema.utils.ResponseWrapper
import com.nestor.schema.utils.combineTransform
import com.nestor.schema.utils.mapBody
import com.nestor.uikit.statusbar.StatusBarType
import com.nestor.uikit.util.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dashboardRepository: DashboardRepository,
    private val coroutineContextProvider: CoroutineContextProvider,
    private val currencyRepository: CurrencyRepository,
    private val statusBarRepository: StatusBarRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    val userDetails: StateFlow<ResponseWrapper<UserDetails>> =
        dashboardRepository.fetchDashboardInfo().map {
            it.mapBody { UserDetails(it.userName, it.dailyPhrase) }
        }.onEach { detailsWrapper ->
            if (detailsWrapper.isLoading) {
                statusBarRepository.updateStatusBar(StatusBarType.LoadingDoubleLineStatusBar)
            } else {
                detailsWrapper.body?.let { details ->
                    details.dailyPhrase?.let {
                        statusBarRepository.updateStatusBar(
                            StatusBarType.TitleAndSubtitle(
                                formatStr(R.string.hello, details.userName), it
                            )
                        )
                    } ?: run {
                        statusBarRepository.updateStatusBar(
                            StatusBarType.LeftTitle(formatStr(R.string.hello, details.userName))
                        )
                    }
                }
            }
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
        viewModelScope.launch(coroutineContextProvider.io()) {
            userDetails.collect()
        }
    }

    fun onDifferentCurrencySelect() {
        viewModelScope.launch(coroutineContextProvider.io()) {
            currencyRepository.fetchCurrencies()
                .map { it.body }
                .filterNotNull()
                .collect { currencies ->
                    val currentCurrency = _userCurrency.value.body?.code ?: "USD"
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

    fun logout() {
        viewModelScope.launch(coroutineContextProvider.io()) {
            authRepository.logout()
        }
    }

    private fun formatStr(resource: Int, arg: String): String {
        return this.context.getString(resource, arg)
    }
}