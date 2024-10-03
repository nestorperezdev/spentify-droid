package com.nestor.uikit.snackbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.button.SYButton

@Preview
@Composable
fun SYSnackbarPreview() {
    SpentifyTheme {
        var showSnackBar by remember { mutableStateOf(false) }
        val snackbarHostState = remember { SnackbarHostState() }
        Scaffold(snackbarHost = {
            SnackbarHost(snackbarHostState) {
                SYSnackbar(data = it)
            }
        }) {
            Box(modifier = Modifier.padding(it)) {
                SYButton(onClick = { showSnackBar = true }, buttonText = "Show snackbar")
            }
        }
        LaunchedEffect(showSnackBar) {
            if (showSnackBar) {
                snackbarHostState.showSnackbar("This is a snackbar")
                showSnackBar = false
            }
        }
    }
}