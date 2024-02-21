package com.nestor.spentify.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.nestor.auth.authScreen
import com.nestor.auth.navigation.AuthGraph
import com.nestor.spentify.navigation.AppNavigationGraph

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = AppNavigationGraph.AuthGraph.route) {
        navigation(
            route = AppNavigationGraph.AuthGraph.route,
            startDestination = AuthGraph.Welcome.route
        ) {
            authScreen(navController)
        }
    }
}