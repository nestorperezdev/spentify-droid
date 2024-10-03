package com.nestor.uikit.button

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nestor.uikit.SpentifyTheme

@Preview
@Composable
fun SYTextButtonPreview() {
    SpentifyTheme {
        Scaffold {
            SYTextButton(
                modifier = Modifier.padding(it),
                buttonText = "Skip",
                onClick = { /*TODO*/ }
            )
        }
    }
}