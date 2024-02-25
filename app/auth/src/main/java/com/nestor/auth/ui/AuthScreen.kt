package com.nestor.auth.ui

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.nestor.auth.ui.login.LoginScreen
import com.nestor.auth.ui.login.LoginViewModel
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
    composable(
        "${AuthGraph.Login.route}?username={username}",
        arguments = listOf(navArgument("username") { nullable = true })
    ) { backstackEntry ->
        LoginScreen(
            onNavigationBackClick = { navController.popBackStack() },
            onSignupClick = { navController.navigate(AuthGraph.Signup.route) },
            initialEmailValue = backstackEntry.arguments?.getString("username"),
        )
    }
    composable(AuthGraph.Signup.route) {
        SignupScreen(
            onLoginClick = {
                val navRoute = if (it != null) {
                    AuthGraph.Login.route + "?username=$it"
                } else {
                    AuthGraph.Login.route
                }
                navController.navigate(navRoute)
            },
            onRecoverPassword = {
            },
            onNavigationBackClick = {
                navController.popBackStack()
            })
    }
}
