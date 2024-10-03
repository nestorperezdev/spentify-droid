package com.nestor.uikit.button

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nestor.uikit.theme.color.Gray

@Composable
fun SYAlternativeButton(
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit,
    buttonText: String
) {
    Button(
        modifier = modifier.heightIn(min = 60.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiary,
            disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f),
            disabledContentColor = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.5f)
        ),
        onClick = onClick,
        shape = MaterialTheme.shapes.small,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
    ) {
        trailingIcon?.invoke()
        if (trailingIcon != null) {
            Spacer(modifier = Modifier.width(14.dp))
        }
        Text(
            text = buttonText,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.W400),
            color = Gray
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}