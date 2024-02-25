package com.nestor.onboarding

import android.provider.CalendarContract.Colors
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.button.SYButton
import com.nestor.uikit.statusbar.SYStatusBar
import com.nestor.uikit.statusbar.StatusBarAction
import com.nestor.uikit.statusbar.StatusBarType
import com.nestor.uikit.stepperdot.SYStepperDot
import com.nestor.uikit.stepperdot.rememberStepperDotState
import com.nestor.uikit.theme.spacing.LocalSYPadding

@Composable
fun OnboardingScreen(
    onSkipClick: () -> Unit,
    onLastStepClick: () -> Unit
) {
    OnboardScreenContent(
        onSkipClick = onSkipClick,
        onLastStepClick = onLastStepClick
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun OnboardScreenContent(
    modifier: Modifier = Modifier,
    onSkipClick: () -> Unit,
    onLastStepClick: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            SYStatusBar(
                StatusBarType.OnlyAction(
                    StatusBarAction(
                        stringResource(R.string.skip),
                        onSkipClick
                    )
                )
            )
        }) {
        val stepperDotState = rememberStepperDotState(size = onboardingScreenList.size)
        val pagerState = rememberPagerState(0) { onboardingScreenList.size }
        val currentDot = stepperDotState.currentDotState.collectAsState().value
        LaunchedEffect(currentDot) {
            pagerState.animateScrollToPage(currentDot)
        }
        LaunchedEffect(pagerState.currentPage) {
            stepperDotState.moveToDotNumber(pagerState.currentPage)
        }
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 17.dp)
        ) {
            HorizontalPager(
                state = pagerState,
                pageSpacing = 12.dp,
                verticalAlignment = Alignment.Top
            ) {
                onboardingScreenList.getOrNull(it)?.let { screenContent ->
                    OnboardScreenContent(
                        modifier = Modifier
                            .fillMaxWidth(),
                        content = screenContent
                    )
                }
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SYStepperDot(state = stepperDotState)
                SYButton(
                    onClick = {
                        if (stepperDotState.isLastDot()) {
                            onLastStepClick()
                        }
                        stepperDotState.nextDot()
                    },
                    buttonText = stringResource(R.string.next)
                )
            }
            Spacer(modifier = Modifier.height(LocalSYPadding.current.screenBottomPadding))
        }
    }
}

private val onboardingScreenList = listOf(
    OnboardingScreenContent(
        title = R.string.register_your_expenses,
        description = R.string.spentify_is_a_simple_and_easy_way_to_track_your_monthly_expenses,
        imageResource = R.drawable.img
    ),
    OnboardingScreenContent(
        title = R.string.get_a_summary_of_your_expenses,
        description = R.string.spentify_is_a_simple_and_easy_way_to_track_your_monthly_expenses,
        imageResource = R.drawable.onboarding_s2
    ),
    OnboardingScreenContent(
        title = R.string.about_spentify,
        description = R.string.spentify_is_a_simple_and_easy_way_to_track_your_monthly_expenses,
        imageResource = R.drawable.onboarding_s3
    )
)

@Composable
private fun OnboardScreenContent(
    modifier: Modifier = Modifier,
    content: OnboardingScreenContent
) {
    Column(modifier) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.scrim)
                .aspectRatio(1f),
            model = content.imageResource,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Text(
            text = stringResource(id = content.title),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 17.dp)
        )
        Text(
            text = stringResource(content.description),
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}

@Preview
@Composable
private fun OnboardScreenContentPreview() {
    SpentifyTheme {
        OnboardScreenContent(
            onSkipClick = {},
            onLastStepClick = {}
        )
    }
}