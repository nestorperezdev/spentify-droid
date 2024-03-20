package com.nestor.dashboard.ui

data class DailySummary(
    val maximalExpense: Double = 0.0,
    val minimalExpense: Double = 0.0,
    val totalExpenses: Double = 0.0,
    val dailyAverageExpense: Double = 0.0,
    val userCurrency: UserCurrency = UserCurrency()
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

