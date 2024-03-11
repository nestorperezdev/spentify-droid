package com.nestor.dashboard.ui

data class DashboardUiState(
    val userName: String = "",
    val dailyPhrase: String? = null,
    val totalExpenses: Double = 0.0,
    val dailyAverageExpense: Double = 0.0,
    val maximalExpense: Double = 0.0,
    val minimalExpense: Double = 0.0,
    val isLoading: Boolean = true,
    val userCurrency: UserCurrency = UserCurrency()
) {
    data class UserCurrency(
        val symbol: String = "$",
        val usdValue: Double = 1.0,
        val code: String = "USD"
    )
}

