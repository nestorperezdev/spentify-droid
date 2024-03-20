package com.nestor.dashboard.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nestor.dashboard.R
import com.nestor.dashboard.ui.summarytiles.SummaryTilesScreen
import com.nestor.schema.utils.ResponseWrapper
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.button.SYAlternativeButton
import com.nestor.uikit.loading.ShimmerSkeletonBox
import com.nestor.uikit.loading.ShimmerSkeletonDoubleLine
import com.nestor.uikit.statusbar.SYStatusBar
import com.nestor.uikit.statusbar.StatusBarType
import com.nestor.uikit.theme.spacing.LocalSYPadding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    DashboardScreenContent(
        modifier = modifier,
        summaryState = viewModel.summary,
    )
}

@Composable
private fun DashboardScreenContent(
    modifier: Modifier = Modifier,
    summaryState: StateFlow<ResponseWrapper<DailySummary>>,
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
        Spacer(modifier = Modifier.height(30.dp))
        SYAlternativeButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { /*TODO*/ },
            buttonText = "Register New Expense",
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_savings_24),
                    contentDescription = null
                )
            }
        )
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
        Scaffold {
            DashboardScreenContent(
                modifier = Modifier
                    .padding(it)
                    .padding(LocalSYPadding.current.screenHorizontalPadding),
                summaryState = MutableStateFlow(
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
}

@Preview
@Composable
private fun DashboardScreenContentLoadingPrev() {
    SpentifyTheme {
        Scaffold {
            DashboardScreenContent(
                summaryState = MutableStateFlow(ResponseWrapper.loading()),
                modifier = Modifier
                    .padding(it)
                    .padding(LocalSYPadding.current.screenHorizontalPadding),
            )
        }
    }
}