package com.nestor.onboarding.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.nestor.uikit.SpentifyTheme

@Preview
@Composable
fun OnboardScreenContentPreview() {
    SpentifyTheme {
        OnboardScreenContent(
            onSkipClick = {},
            onLastStepClick = {}
        )
    }
}