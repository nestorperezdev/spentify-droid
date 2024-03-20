package com.nestor.dashboard.ui.summarytiles

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nestor.dashboard.R
import com.nestor.dashboard.ui.DailySummary
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.stepperdot.SYStepperDot
import com.nestor.uikit.stepperdot.rememberStepperDotState
import com.nestor.uikit.util.formatMoneyAmount

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SummaryTilesScreen(
    modifier: Modifier = Modifier,
    summary: DailySummary
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16 / 9f)
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        val stepperState = rememberStepperDotState(size = tileList.size)
        val pagerState = rememberPagerState { tileList.size }
        LaunchedEffect(pagerState.currentPage) {
            stepperState.moveToDotNumber(pagerState.currentPage)
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(bottom = 24.dp)
        ) {
            tileList.getOrNull(it)?.invoke(summary)
        }
        SYStepperDot(
            state = stepperState,
            dotColor = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

val summaryTileScreen1 = @Composable { summary: DailySummary ->
    Column(
        modifier = Modifier
            .fillMaxWidth(),
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
                summary.userCurrency.symbol,
                summary.totalExpenses.formatMoneyAmount()
            ),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

val summaryTileScreen2 = @Composable { summary: DailySummary ->
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = "Your daily average expense",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(
                R.string.currency_format,
                summary.userCurrency.symbol,
                summary.dailyAverageExpense.formatMoneyAmount()
            ),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}


val summaryTileScreen3 = @Composable { summary: DailySummary ->
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = stringResource(R.string.your_highest_expense),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = stringResource(
                R.string.currency_format,
                summary.userCurrency.symbol,
                summary.maximalExpense.formatMoneyAmount()
            ),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.your_lowest_expense),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = stringResource(
                R.string.currency_format,
                summary.userCurrency.symbol,
                summary.minimalExpense.formatMoneyAmount()
            ),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

val tileList = listOf(
    summaryTileScreen1,
    summaryTileScreen2,
    summaryTileScreen3
)


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