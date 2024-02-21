package com.nestor.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.nestor.auth.login.LoginScreen
import com.nestor.auth.navigation.AuthGraph
import com.nestor.auth.signup.SignupScreen
import com.nestor.auth.welcome.WelcomeScreen

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
        SignupScreen {
            navController.popBackStack()
        }
    }
}
