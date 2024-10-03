package com.nestor.dashboard.ui.summarytiles

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.nestor.dashboard.ui.DailySummary
import com.nestor.uikit.SpentifyTheme

@Preview
@Composable
fun SummaryTilesScreenPrev() {
    SpentifyTheme {
        SummaryTilesScreen(
            summary = DailySummary(
                maximalExpense = 100.0,
                dailyAverageExpense = 20.0,
                minimalExpense = 10.0,
                totalExpenses = 155.0
            )
        )
    }
}