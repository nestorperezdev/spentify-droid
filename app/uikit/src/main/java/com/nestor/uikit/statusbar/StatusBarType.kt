package com.nestor.uikit.statusbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign

sealed class StatusBarType(
    val navigationIcon: NavigationIcon? = null,
    val action: StatusBarAction? = null,
    val title: StatusBarTitle? = null,
    val subtitle: StatusBarTitle? = null,
    val topPadding: StatusTopPadding = StatusTopPadding.None
) {
    object LoadingDoubleLineStatusBar : StatusBarType(topPadding = StatusTopPadding.Big)
    class OnlyNavigation(navigationIcon: NavigationIcon) : StatusBarType(navigationIcon)
    class OnlyAction(action: StatusBarAction) : StatusBarType(action = action)
    class NavigationWithTitle(title: String, navigation: NavigationIcon) :
        StatusBarType(navigation, title = StatusBarTitle.LeftSmall(title))

    class CenterTitle(title: String) : StatusBarType(title = StatusBarTitle.SmallCenterTitle(title))

    class TitleAndSubtitle(title: String, subtitle: String) :
        StatusBarType(
            title = StatusBarTitle.MediumTitle(title),
            subtitle = StatusBarTitle.SubtitleSmall(subtitle),
            topPadding = StatusTopPadding.Big
        )

    class LeftTitle(title: String) :
        StatusBarType(
            title = StatusBarTitle.LeftBig(title),
            topPadding = StatusTopPadding.Big
        )
}

sealed class NavigationIcon(val icon: ImageVector, val onClick: () -> Unit) {
    class Back(onClick: () -> Unit) : NavigationIcon(Icons.Filled.KeyboardArrowLeft, onClick)
    class Close(onClick: () -> Unit) : NavigationIcon(Icons.Filled.Close, onClick)
}

class StatusBarAction(val text: String, val onClick: () -> Unit)

sealed class StatusBarTitle(
    val title: String,
    val alignment: TextAlign,
    val style: StatusBarTextStyle
) {
    class LeftSmall(text: String) : StatusBarTitle(text, TextAlign.Start, StatusBarTextStyle.Small)
    class LeftBig(text: String) : StatusBarTitle(text, TextAlign.Start, StatusBarTextStyle.Big)
    class SmallCenterTitle(text: String) :
        StatusBarTitle(text, TextAlign.Center, StatusBarTextStyle.Small)

    class MediumTitle(text: String) :
        StatusBarTitle(text, TextAlign.Start, StatusBarTextStyle.Medium)

    class SubtitleSmall(text: String) :
        StatusBarTitle(text, TextAlign.Start, StatusBarTextStyle.Thin)
}

enum class StatusBarTextStyle {
    Big,
    Medium,
    Small,
    Thin,
}

enum class StatusTopPadding {
    None,
    Big
}

@Composable
fun StatusBarTextStyle.getStyleFromStatusBarTextStyle() =
    when (this) {
        StatusBarTextStyle.Small -> MaterialTheme.typography.titleSmall
        StatusBarTextStyle.Big -> MaterialTheme.typography.titleMedium
        StatusBarTextStyle.Thin -> MaterialTheme.typography.labelMedium
        StatusBarTextStyle.Medium -> MaterialTheme.typography.headlineMedium
    }