package com.nestor.dashboard.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import com.nestor.dashboard.ui.summarytiles.SummaryTilesScreen
import com.nestor.database.data.dashboard.DashboardEntity
import com.nestor.schema.utils.ResponseWrapper
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.loading.ShimmerSkeletonBox
import com.nestor.uikit.loading.ShimmerSkeletonDoubleLine
import com.nestor.uikit.statusbar.SYStatusBar
import com.nestor.uikit.statusbar.StatusBarType
import com.nestor.uikit.theme.spacing.LocalSYPadding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun DashboardScreen(viewModel: DashboardViewModel = hiltViewModel()) {
    DashboardScreenContent(
        dashboardState = viewModel.dashboardInfo,
    )
}

@Composable
private fun DashboardScreenContent(
    dashboardState: StateFlow<ResponseWrapper<DashboardEntity>>,
) {
    val dashboard = dashboardState.collectAsState().value
    val isLoading = dashboard.isLoading
    Scaffold(
        topBar = {
            Column {
                Spacer(modifier = Modifier.height(40.dp))
                if (isLoading) {
                    ShimmerSkeletonDoubleLine(
                        modifier = Modifier.padding(
                            horizontal = LocalSYPadding.current.screenHorizontalPadding,
                            vertical = 18.dp
                        )
                    )
                } else {
                    dashboard.body?.let { dash ->
                        val toolbarType = if (dash.dailyPhrase != null) {
                            StatusBarType.TitleAndSubtitle(
                                stringResource(R.string.hello, dash.userName),
                                dash.dailyPhrase!!
                            )
                        } else {
                            StatusBarType.LeftTitle(stringResource(R.string.hello, dash.userName))
                        }
                        SYStatusBar(toolbarType)
                    }
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = LocalSYPadding.current.screenHorizontalPadding)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            if (isLoading) {
                ShimmerSkeletonBox()
            } else {
                dashboard.body?.let { dash ->
                    SummaryTilesScreen(dash = dash)
                }
            }
        }
    }
}

@Preview
@Composable
private fun DashboardScreenContentPrev() {
    SpentifyTheme {
        DashboardScreenContent(
            dashboardState = MutableStateFlow(
                ResponseWrapper(
                    body = DashboardEntity(
                        userName = "Nestor",
                        dailyPhrase = "Good morning remmeber to save! 💸",
                        userUuid = "",
                        totalExpenses = 50_000.0,
                        maximalExpense = 20.0,
                        minimalExpense = 10.0,
                        dailyAverageExpense = 12.0
                    )
                )
            )
        )
    }
}

@Preview
@Composable
private fun DashboardScreenContentLoadingPrev() {
    SpentifyTheme {
        DashboardScreenContent(
            dashboardState = MutableStateFlow(ResponseWrapper(isLoading = true))
        )
    }
}