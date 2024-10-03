package com.nestor.uikit.input

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.input.action.Action
import com.nestor.uikit.input.action.InputFieldAction

@Preview("Value and Label", showBackground = true)
@Composable
fun SYInputFieldPreview() {
    var field by remember { mutableStateOf(FormFieldData("")) }
    SpentifyTheme {
        SYInputField(
            value = field.value,
            onValueChange = { field = field.copy(value = it) },
            label = "Label",
            modifier = Modifier.padding(32.dp),
            isError = field.errorResource != null,
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

@Preview("Placegolder, Label and Trailing Icon", showBackground = true)
@Composable
fun SYInputFieldPlaceholderTrailingPreview() {
    SpentifyTheme {
        SYInputField(
            value = "",
            onValueChange = {},
            label = "Label",
            placeholder = "Placeholder",
            action = InputFieldAction.TrailingAction(Action(Icons.Default.Email) {}),
            modifier = Modifier.padding(32.dp)
        )
    }
}