package com.nestor.uikit.snackbar

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SYSnackbar(data: SnackbarData) {
    Snackbar {
        Text(data.visuals.message, color = MaterialTheme.colorScheme.primary)
    }
}
