package com.nestor.account.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.nestor.uikit.statusbar.NavigationIcon
import com.nestor.uikit.statusbar.SYStatusBar
import com.nestor.uikit.statusbar.StatusBarType
import com.nestor.uikit.theme.spacing.LocalSYPadding

@Composable
internal fun DialogBase(
    onDismissRequest: () -> Unit = {},
    statusBarTitle: String,
    content: @Composable (modifier: Modifier) -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Scaffold(topBar = {
            SYStatusBar(
                barType = StatusBarType.NavigationWithTitle(
                    title = statusBarTitle,
                    navigation = NavigationIcon.Back(onClick = onDismissRequest)
                )
            )
        }) {
            content(
                Modifier
                    .padding(it)
                    .padding(LocalSYPadding.current.screenHorizontalPadding)
            )
        }
    }
}