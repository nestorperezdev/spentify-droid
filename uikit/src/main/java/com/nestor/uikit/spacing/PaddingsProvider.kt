package com.nestor.uikit.spacing

import androidx.compose.runtime.compositionLocalOf

val LocalSYPadding = compositionLocalOf<SYPadding>(defaultFactory = { SYPadding.SYPaddingDefault })