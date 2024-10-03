package com.nestor.uikit.statusbar

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.nestor.uikit.SpentifyTheme

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