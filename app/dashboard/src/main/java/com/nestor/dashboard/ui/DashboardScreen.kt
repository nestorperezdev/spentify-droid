package com.nestor.dashboard.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nestor.dashboard.R
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.loading.ShimmerSkeletonBox
import com.nestor.uikit.loading.ShimmerSkeletonDoubleLine
import com.nestor.uikit.statusbar.SYStatusBar
import com.nestor.uikit.statusbar.StatusBarType
import com.nestor.uikit.theme.spacing.LocalSYPadding

@Composable
fun DashboardScreen(viewModel: DashboardViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.collectAsState().value
    DashboardScreenContent(
        name = uiState.name,
        dailyPhrase = uiState.dailyPhrase,
        isLoading = uiState.fetchingData
    )
}

@Composable
private fun DashboardScreenContent(
    name: String,
    dailyPhrase: String? = null,
    isLoading: Boolean = false
) {
    Scaffold(
        topBar = {
            if (isLoading) {
                ShimmerSkeletonDoubleLine(
                    modifier = Modifier.padding(
                        horizontal = LocalSYPadding.current.screenHorizontalPadding,
                        vertical = 18.dp
                    )
                )
            } else {
                val toolbarType = if (dailyPhrase != null) {
                    StatusBarType.TitleAndSubtitle(
                        stringResource(R.string.hello, name),
                        dailyPhrase
                    )
                } else {
                    StatusBarType.LeftTitle(stringResource(R.string.hello, name))
                }
                SYStatusBar(toolbarType)
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = LocalSYPadding.current.screenHorizontalPadding)
        ) {
            if (isLoading) {
                ShimmerSkeletonBox()
            }
        }
    }
}

@Preview
@Composable
private fun DashboardScreenContentPrev() {
    SpentifyTheme {
        DashboardScreenContent(name = "Nestor", dailyPhrase = "Good morning, remember to save! 💸")
    }
}

@Preview
@Composable
private fun DashboardScreenContentLoadingPrev() {
    SpentifyTheme {
        DashboardScreenContent(
            name = "Nestor",
            dailyPhrase = "Good morning, remember to save! 💸",
            isLoading = true
        )
    }
}