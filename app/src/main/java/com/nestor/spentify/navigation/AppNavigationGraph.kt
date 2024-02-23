package com.nestor.spentify.navigation

sealed class AppNavigationGraph(val route: String) {
    object AuthGraph : AppNavigationGraph("auth")
    object Home : AppNavigationGraph("home")
}