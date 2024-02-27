package com.nestor.auth.ui.navigation

sealed class AuthGraph(val route: String) {
    object Welcome : AuthGraph("welcome")
    object Login : AuthGraph("login")
    object Signup : AuthGraph("signup")
    object ForgotPassword : AuthGraph("forgot-password")
    object RecoverPassword : AuthGraph("recover-password")
}