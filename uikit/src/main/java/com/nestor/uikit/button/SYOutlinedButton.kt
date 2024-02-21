package com.nestor.uikit.button

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nestor.uikit.SpentifyTheme

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