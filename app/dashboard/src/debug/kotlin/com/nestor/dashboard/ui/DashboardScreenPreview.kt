package com.nestor.dashboard.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nestor.schema.utils.ResponseWrapper
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.theme.spacing.LocalSYPadding
import kotlinx.coroutines.flow.MutableStateFlow

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
fun DashboardScreenContentLoadingPrev() {
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