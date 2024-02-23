package com.nestor.uikit.input.action

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

data class Action(
    val icon: ImageVector,
    val handler: () -> Unit
) {
    @Composable
    fun Content(modifier: Modifier = Modifier) {
        Icon(
            imageVector = icon,
            contentDescription = "",
            modifier = modifier.clickable { handler() })
    }
}

sealed class InputFieldAction(
    val trailingAction: Action? = null,
    val leadingAction: Action? = null,
) {
    class TrailingAction(action: Action) : InputFieldAction(trailingAction = action)
    class LeadingAction(action: Action) : InputFieldAction(leadingAction = action)
}