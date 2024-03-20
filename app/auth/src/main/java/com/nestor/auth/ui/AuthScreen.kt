package com.nestor.auth.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.nestor.auth.ui.forgotpassword.ForgotPasswordScreen
import com.nestor.auth.ui.login.LoginScreen
import com.nestor.auth.ui.navigation.AuthGraph
import com.nestor.auth.ui.recoverpassword.RecoverPasswordScreen
import com.nestor.auth.ui.signup.SignupScreen
import com.nestor.auth.ui.welcome.WelcomeScreen
import com.nestor.uikit.animation.syEnterTransition
import com.nestor.uikit.animation.syExitTransition
import com.nestor.uikit.animation.syPopEnterTransition
import com.nestor.uikit.animation.syPopExitTransition
import com.nestor.uikit.util.isRouteOnPreviousStack

private const val TAG = "AuthScreen"

@SuppressLint("RestrictedApi")
fun NavGraphBuilder.authScreen(navController: NavHostController, onSuccessfulLogin: () -> Unit) {
    composable(AuthGraph.Welcome.route) {
        WelcomeScreen(
            onLoginClick = {
                navController.navigate(AuthGraph.Login.route)
            },
            onSignupClick = {
                navController.navigate(AuthGraph.Signup.route)
            }
        )
    }
    composable(
        "${AuthGraph.Login.route}?username={username}",
        arguments = listOf(navArgument("username") { nullable = true }),
        enterTransition = { syEnterTransition },
        exitTransition = { syExitTransition },
        popExitTransition = { syPopExitTransition },
        popEnterTransition = { syPopEnterTransition }
    ) { backstackEntry ->
        LoginScreen(
            onNavigationBackClick = { navController.popBackStack() },
            onSignupClick = {
                if (navController.isRouteOnPreviousStack(AuthGraph.Signup.route)) {
                    navController.popBackStack(AuthGraph.Signup.route, false)
                } else {
                    navController.navigate(AuthGraph.Signup.route)
                }
            },
            initialEmailValue = backstackEntry.arguments?.getString("username"),
            onForgotPasswordClick = { navController.navigate(AuthGraph.ForgotPassword.route) },
            onSuccessLogin = {
                onSuccessfulLogin()
            }
        )
    }
    composable(
        AuthGraph.Signup.route,
        enterTransition = { syEnterTransition },
        exitTransition = { syExitTransition },
        popExitTransition = { syPopExitTransition },
        popEnterTransition = { syPopEnterTransition }
    ) {
        SignupScreen(
            onLoginClick = {
                val navRoute = if (it != null) {
                    AuthGraph.Login.route + "?username=$it"
                } else {
                    AuthGraph.Login.route
                }
                if (navController.isRouteOnPreviousStack(AuthGraph.Login.route)) {
                    navController.popBackStack(AuthGraph.Login.route, false)
                } else {
                    navController.navigate(navRoute)
                }
            },
            onRecoverPassword = {
                //TODO: Implement recover password
            },
            onNavigationBackClick = {
                navController.popBackStack()
            },
            onSuccessLogin = {
                onSuccessfulLogin()
            }
        )
    }

    composable(
        AuthGraph.ForgotPassword.route,
        enterTransition = { syEnterTransition },
        exitTransition = { syExitTransition },
        popExitTransition = { syPopExitTransition },
        popEnterTransition = { syPopEnterTransition }
    ) {
        ForgotPasswordScreen(
            onNavigationBackClick = { navController.popBackStack() }
        )
    }

    composable(
        AuthGraph.RecoverPassword.route,
        enterTransition = { syEnterTransition },
        exitTransition = { syExitTransition },
        popExitTransition = { syPopExitTransition },
        popEnterTransition = { syPopEnterTransition },
        deepLinks = listOf(navDeepLink {
            uriPattern = "spentify://${AuthGraph.RecoverPassword.route}"
            action = Intent.ACTION_VIEW
        })
    ) {
        val token = it.arguments?.getString("token")
        Log.i(TAG, "authScreen: $token")
        RecoverPasswordScreen(onNavigationBackClick = { })
    }
}
