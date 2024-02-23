package com.nestor.auth.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.nestor.auth.ui.login.LoginScreen
import com.nestor.auth.ui.navigation.AuthGraph
import com.nestor.auth.ui.signup.SignupScreen
import com.nestor.auth.ui.welcome.WelcomeScreen

fun NavGraphBuilder.authScreen(navController: NavHostController) {
    composable(AuthGraph.Welcome.route) {
        WelcomeScreen(
            onLoginClick = { navController.navigate(AuthGraph.Login.route) },
            onSignupClick = { navController.navigate(AuthGraph.Signup.route) }
        )
    }
    composable(AuthGraph.Login.route) {
        LoginScreen {
            navController.popBackStack()
        }
    }
    composable(AuthGraph.Signup.route) {
        SignupScreen(
            onLoginClick = {
                navController.navigate(AuthGraph.Login.route)
            },
            onRecoverPassword = {
            },
            onNavigationBackClick = {
                navController.popBackStack()
            })
    }
}
