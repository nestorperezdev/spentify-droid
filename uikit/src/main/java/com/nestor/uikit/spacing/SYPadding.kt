package com.nestor.uikit.spacing

import androidx.compose.ui.unit.Dp

sealed class SYPadding(
    val screenHorizontalPadding: Dp,
    val spacingBig2x: Dp,
    val spacingBig: Dp,
    val spacingMid: Dp,
    val spacingSmall: Dp,
) {
    object SYPaddingDefault : SYPadding(
        screenHorizontalPadding = Dp(16f),
        spacingBig2x = Dp(24f),
        spacingBig = Dp(16f),
        spacingMid = Dp(12f),
        spacingSmall = Dp(8f),
    )
}