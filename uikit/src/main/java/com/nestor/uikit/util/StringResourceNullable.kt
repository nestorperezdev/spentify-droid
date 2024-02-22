package com.nestor.uikit.util

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource

@Composable
@ReadOnlyComposable
fun stringResourceNullable(@StringRes id: Int?): String? {
    return id?.let { stringResource(it) }
}