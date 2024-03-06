package com.nestor.dashboard.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.statusbar.SYStatusBar
import com.nestor.uikit.statusbar.StatusBarType

@Composable
fun DashboardScreen() {
    DashboardScreenContent()
}

@Composable
private fun DashboardScreenContent() {
    Scaffold(
        topBar = {
            SYStatusBar(
                StatusBarType.TitleAndSubtitle(
                    "Hello Joseph",
                    "Don't forget to save!"
                )
            )
        }
    ) {
        Box(modifier = Modifier.padding(it))
    }
}

@Preview
@Composable
private fun DashboardScreenContentPrev() {
    SpentifyTheme {
        DashboardScreenContent()
    }
}