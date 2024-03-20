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
        userDetails = viewModel.userDetails,
        summary = viewModel.summary,
        onDifferentCurrencySelect = viewModel::onDifferentCurrencySelect
    )
}

@Composable
private fun DashboardScreenContent(
    userDetails: StateFlow<ResponseWrapper<UserDetails>>,
    summary: StateFlow<ResponseWrapper<DailySummary>>,
    onDifferentCurrencySelect: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            DashboardScreenToolbar(userDetails = userDetails)
        }
    ) {
        DashboardScreenSummaryContent(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = LocalSYPadding.current.screenHorizontalPadding),
            summaryState = summary,
            onDifferentCurrencySelect = onDifferentCurrencySelect
        )
    }
}

@Composable
private fun DashboardScreenSummaryContent(
    modifier: Modifier = Modifier,
    summaryState: StateFlow<ResponseWrapper<DailySummary>>,
    onDifferentCurrencySelect: () -> Unit = {}
) {
    val summaryWrapper = summaryState.collectAsState().value
    Column(
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        if (summaryWrapper.isLoading) {
            ShimmerSkeletonBox()
        } else {
            summaryWrapper.body?.let { summary ->
                SummaryTilesScreen(summary = summary)
            }
        }
        summaryWrapper.body?.userCurrency?.let {
            SYButton(
                onClick = onDifferentCurrencySelect,
                buttonText = "Toggle different currency"
            )
        }
        val vm: DashboardViewModel = hiltViewModel()
        SYButton(onClick = { vm.logout() }, buttonText = "Logout test")
    }
}

@Composable
private fun DashboardScreenToolbar(
    modifier: Modifier = Modifier,
    userDetails: StateFlow<ResponseWrapper<UserDetails>>
) {
    val responseWrapper = userDetails.collectAsState().value
    Column {
        Spacer(modifier = modifier.height(40.dp))
        if (responseWrapper.isLoading) {
            ShimmerSkeletonDoubleLine(
                modifier = Modifier.padding(
                    horizontal = LocalSYPadding.current.screenHorizontalPadding,
                    vertical = 18.dp
                )
            )
        } else {
            responseWrapper.body?.let { userDetails ->
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


@Preview
@Composable
private fun DashboardScreenContentPrev() {
    SpentifyTheme {
        DashboardScreenContent(
            userDetails = MutableStateFlow(
                ResponseWrapper.success(
                    UserDetails(
                        userName = "Nestor",
                        dailyPhrase = "Good morning, Remember to save today 💸",
                    )
                )
            ),
            summary = MutableStateFlow(
                ResponseWrapper.success(
                    DailySummary(
                        totalExpenses = 1723.50,
                        minimalExpense = 500.0,
                        maximalExpense = 501.76,
                        dailyAverageExpense = 123.10,
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
            userDetails = MutableStateFlow(ResponseWrapper.loading()),
            summary = MutableStateFlow(ResponseWrapper.loading())
        )
    }
}