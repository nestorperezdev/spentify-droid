package com.nestor.auth.ui.welcome

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.nestor.uikit.SpentifyTheme

@Preview
@Composable
fun LoginScreenPreview() {
    SpentifyTheme {
        WelcomeScreenContent({}, {})
    }
}