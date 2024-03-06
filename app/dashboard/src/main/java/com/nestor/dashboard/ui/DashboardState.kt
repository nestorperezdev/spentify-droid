package com.nestor.dashboard.ui

data class DashboardState(
    val name: String = "",
    val dailyPhrase: String? = null,
    val fetchingData: Boolean = true
)
