package com.nestor.uikit

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.nestor.uikit.theme.color.DarkColorScheme
import com.nestor.uikit.theme.color.LightColorScheme
import com.nestor.uikit.theme.color.LocalSYColorScheme
import com.nestor.uikit.theme.image.LocalSYImageServerProvider
import com.nestor.uikit.theme.spacing.LocalSYPadding
import com.nestor.uikit.theme.spacing.SYPadding
import com.nestor.uikit.theme.typography.getTypo

@Composable
fun SpentifyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    imageServerUrl: String = "https://dev.nestorperez.dev/3000",
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
            window.statusBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }
    CompositionLocalProvider(
        LocalSYColorScheme provides colorScheme,
        LocalSYPadding provides SYPadding.SYPaddingDefault,
        LocalSYImageServerProvider provides imageServerUrl,

    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = getTypo(),
            content = content
        )
    }
}
