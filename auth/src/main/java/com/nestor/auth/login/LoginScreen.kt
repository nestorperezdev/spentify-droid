package com.nestor.auth.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.statusbar.NavigationIcon
import com.nestor.uikit.statusbar.SYStatusBar
import com.nestor.uikit.statusbar.StatusBarType
import com.nestor.uikit.theme.spacing.LocalSYPadding

@Composable
fun LoginScreen(onNavigationBackClick: () -> Unit) {
    LoginScreenContent(onNavigationBackClick = onNavigationBackClick)
}

@Composable
private fun LoginScreenContent(onNavigationBackClick: () -> Unit) {
    Scaffold(topBar = {
        SYStatusBar(
            barType = StatusBarType.OnlyNavigation(navigationIcon =
            NavigationIcon.Close {
                onNavigationBackClick()
            })
        )
    }) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(bottom = LocalSYPadding.current.screenBottomPadding)
                .padding(horizontal = LocalSYPadding.current.screenHorizontalPadding)
                .padding(top = 70.dp)
        ) {
            Text(
                text = "Sign into your Account",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.widthIn(max = 240.dp)
            )
            Text(text = "Log into your Spentify account.")
        }
    }
}

@Preview
@Composable
fun LoginScreenContentPreview() {
    SpentifyTheme {
        LoginScreenContent {}
    }
}