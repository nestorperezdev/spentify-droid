package com.nestor.uikit.color

import androidx.compose.ui.graphics.Color

sealed class SYColorPalette(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
) {
    object SYColorPaletteDark : SYColorPalette(
        primary = Blue80,
        secondary = Blue10,
        tertiary = Color(0xFF000000),
    )

    object SYColorPaletteLight : SYColorPalette(
        primary = Blue80,
        secondary = Blue10,
        tertiary = Color(0xFF000000),
    )
}
