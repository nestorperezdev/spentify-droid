package com.nestor.uikit.input

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nestor.uikit.SpentifyTheme

@Composable
fun SYInputField(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String? = null,
    label: String? = null,
    onValueChange: (String) -> Unit
) {
    Column(modifier = modifier) {
        label?.let {
            Text(
                text = it, modifier = Modifier
                    .padding(bottom = 10.dp)
                    .fillMaxWidth()
            )
        }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder?.let { { Text(it) } })
    }
}

@Preview("Value and Label", showBackground = true)
@Composable
fun SYInputFieldPreview() {
    SpentifyTheme {
        SYInputField(
            value = "Hello",
            onValueChange = {},
            label = "Label",
            modifier = Modifier.padding(32.dp)
        )
    }
}

@Preview("Placegolder and Label", showBackground = true)
@Composable
fun SYInputFieldPlaceholderPreview() {
    SpentifyTheme {
        SYInputField(
            value = "",
            onValueChange = {},
            label = "Label",
            placeholder = "Placeholder",
            modifier = Modifier.padding(32.dp)
        )
    }
}