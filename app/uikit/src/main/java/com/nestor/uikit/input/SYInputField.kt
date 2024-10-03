package com.nestor.uikit.input

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
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