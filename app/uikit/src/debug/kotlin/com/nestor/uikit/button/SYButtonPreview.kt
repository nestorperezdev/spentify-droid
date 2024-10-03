package com.nestor.uikit.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.nestor.uikit.SpentifyTheme

@Preview
@Composable
fun SYButtonPreview() {
    SpentifyTheme {
        SYButton(
            buttonText = "Hello, Spentify!",
            onClick = { /*TODO*/ }
        )
    }
}