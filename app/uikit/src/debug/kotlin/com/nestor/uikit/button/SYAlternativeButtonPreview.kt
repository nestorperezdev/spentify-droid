package com.nestor.uikit.button

import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nestor.uikit.SpentifyTheme

@Preview
@Composable
fun SYAlternativeButtonPreview() {
    SpentifyTheme {
        SYAlternativeButton(
            buttonText = "Hello, Spentify!",
            onClick = { /*TODO*/ },
            trailingIcon = {
                Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null)
            },
            modifier = Modifier.width(480.dp)
        )
    }
}