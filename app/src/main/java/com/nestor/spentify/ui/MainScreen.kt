package com.nestor.spentify.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.nestor.auth.ui.authScreen
import com.nestor.auth.ui.navigation.AuthGraph
import com.nestor.dashboard.ui.DashboardScreen
import com.nestor.expenses.ui.NewExpenseScreen
import com.nestor.onboarding.ui.OnboardingScreen
import com.nestor.spentify.navigation.AppHomeNavigationGraph
import com.nestor.spentify.navigation.AppNavigationGraph
import com.nestor.uikit.button.SYButton
import com.nestor.uikit.navigation.SYBottomNavBarData
import com.nestor.uikit.statusbar.SYStatusBar
import com.nestor.uikit.statusbar.StatusBarType
import com.nestor.uikit.theme.spacing.LocalSYPadding
import kotlinx.coroutines.launch

@Composable
fun MainScreen(mainViewModel: MainViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    NavHost(
        navController, startDestination = mainViewModel.navRoute.collectAsState().value
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
            OnboardingScreen(onSkipClick = {
                mainViewModel.onOnboardingFinished()
                navController.popBackStack()
            }, onLastStepClick = {
                mainViewModel.onOnboardingFinished()
                navController.popBackStack()
            })
        }
        composable(route = AppNavigationGraph.Home.route) {
            HomeScreen(mainViewModel.statusBarType.collectAsState().value,
                onNewExpenseClick = {
                    navController.navigate(AppNavigationGraph.NewExpense.route)
                }
            )
        }
        composable(route = AppNavigationGraph.NewExpense.route) {
            NewExpenseScreen {
                navController.popBackStack()
            }
        }
    }
}