package com.nestor.uikit.button

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.nestor.uikit.SpentifyTheme


@Composable
fun SYTextButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    buttonText: String
) {
    TextButton(
        onClick = onClick,
        shape = MaterialTheme.shapes.extraLarge,
        modifier = modifier
    ) {
        Text(
            text = buttonText.uppercase(),
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.W300
        )
    }
}

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