package com.nestor.uikit.button

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun SYOutlinedButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    buttonText: String
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        shape = MaterialTheme.shapes.extraSmall
    ) {
        Text(
            text = buttonText.uppercase(),
            letterSpacing = 1.sp
        )
    }
}