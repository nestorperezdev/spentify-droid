package com.nestor.uikit.button

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight


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
