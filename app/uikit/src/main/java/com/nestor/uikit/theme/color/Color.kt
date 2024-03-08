package com.nestor.uikit.theme.color

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

internal val Blue80 = Color(0xFF0066F6)
internal val Blue10 = Color(0xFF001533)
val Pink40 = Color(0xFF7D5260)
val warningLight = Color(0xFFFF647C)
val inactive = Color(0xFFC4C4C4)

internal val DarkColorScheme = darkColorScheme(
    primary = Blue80,
    onPrimary = Color.White,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    error = warningLight,
    scrim = inactive
)

internal val LightColorScheme = lightColorScheme(
    primary = Blue80,
    secondary = Blue10,
    tertiary = Pink40,
    onPrimary = Color.White,
    error = warningLight,
    scrim = inactive

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)