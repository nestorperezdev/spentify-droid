package com.nestor.auth.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.statusbar.NavigationIcon
import com.nestor.uikit.statusbar.SYStatusBar
import com.nestor.uikit.statusbar.StatusBarType
import com.nestor.uikit.theme.spacing.LocalSYPadding

@Composable
fun SignupScreen(onNavigationBackClick: () -> Unit) {
    SignupScreenContent(onNavigationBackClick = onNavigationBackClick)
}

@Composable
private fun SignupScreenContent(onNavigationBackClick: () -> Unit) {
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
            Text(text = "Create Account", style = MaterialTheme.typography.titleLarge)
            Text(text = "Open a Spentify account with few details.")
        }
    }
}

@Preview
@Composable
fun SignupScreenContentPreview() {
    SpentifyTheme {
        SignupScreenContent {}
    }
}