package com.nestor.dashboard.ui

import com.nestor.schema.utils.ResponseWrapper

data class DailySummary(
    val maximalExpense: Double = 0.0,
    val minimalExpense: Double = 0.0,
    val totalExpenses: Double = 0.0,
    val dailyAverageExpense: Double = 0.0,
)

data class UserCurrency(
    val symbol: String = "$",
    val usdValue: Double = 1.0,
    val code: String = "USD"
)

data class UserDetails(
    val userName: String = "",
    val dailyPhrase: String? = null,
)

data class DashboardUiState(
    val userDetails: ResponseWrapper<UserDetails> = ResponseWrapper.loading(),
    val userCurrency: ResponseWrapper<UserCurrency> = ResponseWrapper.loading(),
    val summary: ResponseWrapper<DailySummary> = ResponseWrapper.loading()
)

