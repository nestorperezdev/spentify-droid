package com.nestor.uikit

import android.app.Activity
import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.nestor.uikit.button.SYButton
import com.nestor.uikit.statusbar.NavigationIcon
import com.nestor.uikit.statusbar.SYStatusBar
import com.nestor.uikit.statusbar.StatusBarType
import com.nestor.uikit.theme.color.DarkColorScheme
import com.nestor.uikit.theme.color.LightColorScheme
import com.nestor.uikit.theme.color.LocalSYColorScheme
import com.nestor.uikit.theme.spacing.LocalSYPadding
import com.nestor.uikit.theme.spacing.SYPadding
import com.nestor.uikit.theme.typography.getTypo

@Composable
fun SpentifyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }
    CompositionLocalProvider(
        LocalSYColorScheme provides colorScheme,
        LocalSYPadding provides SYPadding.SYPaddingDefault
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = getTypo(),
            content = content
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun SpentifyThemePreview() {
    SpentifyTheme {
        Scaffold(
            topBar = {
                SYStatusBar(barType = StatusBarType.OnlyNavigation(NavigationIcon.Close { }))
            }
        ) {
            Column(
                Modifier
                    .fillMaxHeight()
                    .padding(it)
                    .padding(
                        start = LocalSYPadding.current.screenHorizontalPadding,
                        end = LocalSYPadding.current.screenHorizontalPadding,
                        bottom = 30.dp
                    ),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(
                    modifier = Modifier.padding(top = 70.dp),
                    verticalArrangement = spacedBy(LocalSYPadding.current.spacingBig2x)
                ) {
                    Text(
                        text = "Hello Spentify!",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "This is an example of how the theme principal elements will look on the UI!",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.widthIn(max = 280.dp)
                    )
                }
                SYButton(
                    onClick = { },
                    buttonText = "Change password",
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.End)
                )
            }
        }
    }
}