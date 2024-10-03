package com.nestor.auth.ui.forgotpassword

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.nestor.uikit.SpentifyTheme

@Preview
@Composable
fun ForgotPasswordPreview() {
    SpentifyTheme {
        ForgotPasswordScreenContent()
    }
}

@Preview
@Composable
fun ForgotPasswordEmailSentPreview() {
    SpentifyTheme {
        ForgotPasswordEmailSent()
    }
}