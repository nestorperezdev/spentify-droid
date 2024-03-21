package com.nestor.spentify.navigation

sealed class AppNavigationGraph(val route: String) {
    object AuthGraph : AppNavigationGraph("auth")
    object Home : AppNavigationGraph("home")
    object Onboarding : AppNavigationGraph("onboarding")
    object Splash : AppNavigationGraph("splash")
}

sealed class AppHomeNavigationGraph(val route: String) {
    object Dashboard : AppHomeNavigationGraph("dashboard")
    object Expenses : AppHomeNavigationGraph("expenses")
    object Profile : AppHomeNavigationGraph("profile")
}
