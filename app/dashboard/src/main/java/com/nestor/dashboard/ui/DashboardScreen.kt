package com.nestor.dashboard.ui

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
import com.nestor.schema.utils.ResponseWrapper
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.button.SYButton
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
        onDifferentCurrencySelect = viewModel::onDifferentCurrencySelect
    )
}

@Composable
private fun DashboardScreenContent(
    dashboardState: StateFlow<DashboardUiState>,
    onDifferentCurrencySelect: () -> Unit = {}
) {
    val dashboard = dashboardState.collectAsState().value
    Scaffold(
        topBar = {
            Column {
                Spacer(modifier = Modifier.height(40.dp))
                if (dashboard.userDetails.isLoading) {
                    ShimmerSkeletonDoubleLine(
                        modifier = Modifier.padding(
                            horizontal = LocalSYPadding.current.screenHorizontalPadding,
                            vertical = 18.dp
                        )
                    )
                } else {
                    dashboard.userDetails.body?.let { userDetails ->
                        val toolbarType = if (userDetails.dailyPhrase != null) {
                            StatusBarType.TitleAndSubtitle(
                                stringResource(R.string.hello, userDetails.userName),
                                userDetails.dailyPhrase
                            )
                        } else {
                            StatusBarType.LeftTitle(
                                stringResource(
                                    R.string.hello,
                                    userDetails.userName
                                )
                            )
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
            if (dashboard.summary.isLoading || dashboard.userCurrency.isLoading) {
                ShimmerSkeletonBox()
            } else {
                dashboard.summary.body?.let { summary ->
                    dashboard.userCurrency.body?.let { currency ->
                        SummaryTilesScreen(summary = summary, currency = currency)
                    }
                }
            }
            dashboard.userCurrency.body?.let {
                SYButton(
                    onClick = onDifferentCurrencySelect,
                    buttonText = "Toggle different currency"
                )
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
                DashboardUiState(
                    userDetails = ResponseWrapper.success(
                        UserDetails(
                            userName = "Nestor",
                            dailyPhrase = "Good morning, Remember to save today 💸",
                        )
                    ),
                    userCurrency = ResponseWrapper.success(
                        UserCurrency(
                            usdValue = 1.0,
                            code = "USD",
                            symbol = "$"
                        )
                    ),
                    summary = ResponseWrapper.success(
                        DailySummary(
                            totalExpenses = 1723.50,
                            minimalExpense = 500.0,
                            maximalExpense = 501.76,
                            dailyAverageExpense = 123.10,
                        )
                    ),
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
            dashboardState = MutableStateFlow(DashboardUiState())
        )
    }
}