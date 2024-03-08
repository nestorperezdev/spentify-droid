package com.nestor.dashboard.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nestor.dashboard.R
import com.nestor.database.data.dashboard.DashboardEntity
import com.nestor.schema.utils.ResponseWrapper
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.loading.ShimmerSkeletonBox
import com.nestor.uikit.loading.ShimmerSkeletonDoubleLine
import com.nestor.uikit.statusbar.SYStatusBar
import com.nestor.uikit.statusbar.StatusBarType
import com.nestor.uikit.stepperdot.SYStepperDot
import com.nestor.uikit.stepperdot.rememberStepperDotState
import com.nestor.uikit.theme.spacing.LocalSYPadding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun DashboardScreen(viewModel: DashboardViewModel = hiltViewModel()) {
    DashboardScreenContent(
        dashboardState = viewModel.dashboardInfo,
    )
}

@OptIn(ExperimentalFoundationApi::class)
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
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16 / 9f)
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    dashboard.body?.let { dash ->
                        val stepperState = rememberStepperDotState(size = 3)
                        val pagerState = rememberPagerState { 3 }
                        LaunchedEffect(pagerState.currentPage) {
                            stepperState.moveToDotNumber(pagerState.currentPage)
                        }
                        HorizontalPager(state = pagerState) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = stringResource(R.string.total_expenses_this_month),
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                Text(
                                    text = stringResource(
                                        R.string.currency_format,
                                        dash.totalExpenses
                                    ),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                )
                            }
                        }
                        SYStepperDot(
                            state = stepperState,
                            dotColor = MaterialTheme.colorScheme.onPrimary
                        )
                    }
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
                        totalExpenses = 50_000.0
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