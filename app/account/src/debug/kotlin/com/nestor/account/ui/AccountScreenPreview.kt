package com.nestor.account.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.theme.spacing.LocalSYPadding
import kotlinx.coroutines.flow.MutableStateFlow

@Preview
@Composable
fun AccountScreenPreview() {
    SpentifyTheme {
        Scaffold {
            AccountScreenContent(
                modifier = Modifier
                    .padding(it)
                    .padding(LocalSYPadding.current.screenHorizontalPadding),
                username = "Nestor",
                appVersion = MutableStateFlow("1.0.0.500.preview")
            )
        }
    }
}