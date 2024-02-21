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
import com.nestor.uikit.color.DarkColorScheme
import com.nestor.uikit.color.LightColorScheme
import com.nestor.uikit.color.LocalSYColorScheme
import com.nestor.uikit.spacing.LocalSYPadding
import com.nestor.uikit.spacing.SYPadding
import com.nestor.uikit.typography.getTypo

@Composable
fun SpentifyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        /*dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }*/

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
        Scaffold {
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