package com.nestor.uikit.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.nestor.uikit.SpentifyTheme

@Preview(showBackground = true)
@Composable
fun SYOutlinedButtonPreview() {
    SpentifyTheme {
        SYOutlinedButton(
            buttonText = "Hello, Spentify!",
            onClick = { /*TODO*/ }
        )
    }
}