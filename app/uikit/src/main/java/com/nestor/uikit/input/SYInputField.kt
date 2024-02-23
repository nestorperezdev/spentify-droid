package com.nestor.uikit.input

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.input.action.Action
import com.nestor.uikit.input.action.InputFieldAction

@Composable
fun SYInputField(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String? = null,
    label: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isError: Boolean = false,
    error: String? = null,
    action: InputFieldAction? = null,
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
            placeholder = placeholder?.let { { Text(it) } },
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            suffix = action?.trailingAction?.let { { it.Content() } },
            prefix = action?.leadingAction?.let { { it.Content() } },
            visualTransformation = visualTransformation,
            isError = isError,
            supportingText = error?.let { { Text(it, color = MaterialTheme.colorScheme.error) } }
        )
    }
}

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