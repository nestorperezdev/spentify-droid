package com.nestor.spentify.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.nestor.auth.ui.authScreen
import com.nestor.auth.ui.navigation.AuthGraph
import com.nestor.dashboard.ui.DashboardScreen
import com.nestor.onboarding.ui.OnboardingScreen
import com.nestor.spentify.navigation.AppNavigationGraph

@Composable
fun MainScreen(mainViewModel: MainViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    NavHost(
        navController,
        startDestination = mainViewModel.navRoute.collectAsState().value
    ) {
        composable(AppNavigationGraph.Splash.route) {
            Text(text = "Splash!")
        }
        navigation(
            route = AppNavigationGraph.AuthGraph.route,
            startDestination = AuthGraph.Welcome.route,
        ) {
            authScreen(navController) {}
        }
        composable(
            route = AppNavigationGraph.Onboarding.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = null,
            popExitTransition = null
        ) {
            OnboardingScreen(
                onSkipClick = {
                    mainViewModel.onOnboardingFinished()
                    navController.popBackStack()
                },
                onLastStepClick = {
                    mainViewModel.onOnboardingFinished()
                    navController.popBackStack()
                }
            )
        }
        composable(route = AppNavigationGraph.Home.route) {
            DashboardScreen()
        }
    }
}