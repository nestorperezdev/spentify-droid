package com.nestor.uikit.statusbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.button.SYTextButton
import com.nestor.uikit.loading.ShimmerSkeletonDoubleLine

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SYStatusBar(barType: StatusBarType) {
    Column {
        val topPadding = when(barType.topPadding) {
            StatusTopPadding.Big -> {
                40.dp
            }
            StatusTopPadding.None -> {
                0.dp
            }
        }
        TopAppBar(
            modifier = Modifier.padding(top = topPadding),
            title = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    if (barType is StatusBarType.LoadingDoubleLineStatusBar) {
                        ShimmerSkeletonDoubleLine()
                    }
                    barType.title?.let {
                        Text(
                            text = it.title,
                            textAlign = it.alignment,
                            style = it.style.getStyleFromStatusBarTextStyle(),
                            modifier = Modifier
                                .padding(start = 12.dp)
                                .fillMaxWidth()
                        )
                    }
                    barType.subtitle?.let {
                        Text(
                            text = it.title,
                            textAlign = it.alignment,
                            style = it.style.getStyleFromStatusBarTextStyle(),
                            modifier = Modifier
                                .padding(start = 12.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            },
            navigationIcon = {
                barType.navigationIcon?.let { icon ->
                    IconButton(onClick = icon.onClick) {
                        Icon(imageVector = icon.icon, contentDescription = "")
                    }
                }
            },
            actions = {
                barType.action?.let {
                    SYTextButton(onClick = it.onClick, buttonText = it.text)
                }
            }
        )
    }
}

@Preview("Loading")
@Composable
fun SYStatusBarPreviewLoading() {
    SpentifyTheme {
        SYStatusBar(StatusBarType.LoadingDoubleLineStatusBar)
    }
}

@Preview("Only Navigation")
@Composable
fun SYStatusBarPreviewNavigation() {
    SpentifyTheme {
        SYStatusBar(StatusBarType.OnlyNavigation(NavigationIcon.Close { }))
    }
}

@Preview("Only Action")
@Composable
fun SYStatusBarPreviewAction() {
    SpentifyTheme {
        SYStatusBar(StatusBarType.OnlyAction(StatusBarAction("Skip") { }))
    }
}

@Preview("Left title and navigation")
@Composable
fun SYStatusBarPreviewLeftAndNavigation() {
    SpentifyTheme {
        SYStatusBar(StatusBarType.NavigationWithTitle("Privacy policy", NavigationIcon.Back { }))
    }
}

@Preview("Center title")
@Composable
fun SYStatusBarPreviewCenterTitle() {
    SpentifyTheme {
        SYStatusBar(StatusBarType.CenterTitle("My Portfolio"))
    }
}

@Preview("Title and Subtitle")
@Composable
fun SYStatusBarPreviewTitleNSubtitle() {
    SpentifyTheme {
        SYStatusBar(
            StatusBarType.TitleAndSubtitle(
                "Hello Joseph",
                "Good morning remember to save today"
            )
        )
    }
}

@Preview("Title")
@Composable
fun SYStatusBarPreviewTitle() {
    SpentifyTheme {
        SYStatusBar(
            StatusBarType.LeftTitle("Hello Joseph")
        )
    }
}