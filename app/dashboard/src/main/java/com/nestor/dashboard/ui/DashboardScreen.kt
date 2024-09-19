package com.nestor.dashboard.ui

import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.nestor.uikit.list.SYList
import com.nestor.uikit.list.SYListItem
import com.nestor.uikit.list.SYListItemData
import com.nestor.uikit.loading.ShimmerSkeletonBox
import com.nestor.uikit.statusbar.StatusBarType
import com.nestor.uikit.statusbar.TopBarTitle
import com.nestor.uikit.theme.spacing.LocalSYPadding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = hiltViewModel(),
    onNewExpenseClick: () -> Unit = {}
) {
    DashboardScreenContent(
        modifier = modifier,
        summaryState = viewModel.summary,
        userDetailsState = viewModel.userDetails,
        onNewExpenseClick = onNewExpenseClick
    )
}

@Composable
private fun DashboardScreenContent(
    modifier: Modifier = Modifier,
    summaryState: StateFlow<ResponseWrapper<DailySummary>>,
    userDetailsState: StateFlow<ResponseWrapper<UserDetails>>,
    onNewExpenseClick: () -> Unit = {}
) {
    val userDetailResponse = userDetailsState.collectAsState().value
    val summaryWrapper = summaryState.collectAsState().value
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        val statusBarType = if (userDetailResponse.body != null) {
            val details = userDetailResponse.body!!
            details.dailyPhrase?.let { daily ->
                StatusBarType.TitleAndSubtitle(
                    title = stringResource(R.string.hello, details.userName),
                    subtitle = daily
                )
            } ?: StatusBarType.LeftTitle(stringResource(R.string.hello, details.userName))
        } else {
            StatusBarType.LoadingDoubleLineStatusBar
        }
        Spacer(modifier = Modifier.height(40.dp))
        TopBarTitle(barType = statusBarType, startPadding = 0.dp)
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
            onClick = { onNewExpenseClick() },
            buttonText = "Register New Expense",
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_savings_24),
                    contentDescription = null
                )
            }
        )
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = "Get your money working for you",
            style = MaterialTheme.typography.titleSmall
        )
        Column(
            modifier = Modifier.padding(top = 20.dp),
            verticalArrangement = spacedBy(20.dp)
        ) {
            SYList(
                items = listOf(
                    SYListItemData(
                        label = "Set a savings goal",
                        leadingIcon = SYListItemData.SYListItemIcon(
                            icon = painterResource(id = R.drawable.baseline_savings_24),
                            tint = MaterialTheme.colorScheme.primary,
                        ),
                        trailingIcon = SYListItemData.SYListItemIcon(
                            icon = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                            tint = MaterialTheme.colorScheme.onSurface,
                            foregroundTint = null
                        ),
                    )
                )
            )
            SYListItem(
                item = SYListItemData(
                    label = "Review your monthly report",
                    leadingIcon = SYListItemData.SYListItemIcon(
                        icon = painterResource(id = R.drawable.baseline_space_dashboard_24),
                        tint = MaterialTheme.colorScheme.primary,
                    ),
                    trailingIcon = SYListItemData.SYListItemIcon(
                        icon = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                        tint = MaterialTheme.colorScheme.onSurface,
                        foregroundTint = null
                    )
                )
            )
        }
        Spacer(modifier = Modifier.height(36.dp))
        Text(
            text = "Categories",
            style = MaterialTheme.typography.titleSmall
        )
        SYListItem(
            modifier = Modifier.padding(top = 20.dp),
            item = SYListItemData(
                label = "Manage your categories",
                leadingIcon = SYListItemData.SYListItemIcon(
                    icon = painterResource(id = R.drawable.baseline_category_24),
                    tint = MaterialTheme.colorScheme.onTertiary,
                ),
                trailingIcon = SYListItemData.SYListItemIcon(
                    icon = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                    tint = MaterialTheme.colorScheme.onSurface,
                    foregroundTint = null
                )
            )
        )
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
                ),
                userDetailsState = MutableStateFlow(
                    ResponseWrapper.success(
                        UserDetails(
                            userName = "Nestor",
                            dailyPhrase = "You are doing great!"
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
                userDetailsState = MutableStateFlow(ResponseWrapper.loading())
            )
        }
    }
}