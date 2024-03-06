package com.nestor.dashboard.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nestor.dashboard.R
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.statusbar.SYStatusBar
import com.nestor.uikit.statusbar.StatusBarType

@Composable
fun DashboardScreen(viewModel: DashboardViewModel = viewModel()) {
    val uiState = viewModel.uiState.collectAsState().value
    DashboardScreenContent(
        name = uiState.name
    )
}

@Composable
private fun DashboardScreenContent(
    name: String,
    dailyPhrase: String? = null
) {
    Scaffold(
        topBar = {
            val toolbarType = if(dailyPhrase != null) {
                StatusBarType.TitleAndSubtitle(
                    stringResource(R.string.hello, name),
                    dailyPhrase
                )
            } else {
                StatusBarType.LeftTitle(stringResource(R.string.hello, name))
            }
            SYStatusBar(toolbarType)
        }
    ) {
        Box(modifier = Modifier.padding(it))
    }
}

@Preview
@Composable
private fun DashboardScreenContentPrev() {
    SpentifyTheme {
        DashboardScreenContent(name = "Nestor", dailyPhrase = "Good morning, remember to save! 💸")
    }
}