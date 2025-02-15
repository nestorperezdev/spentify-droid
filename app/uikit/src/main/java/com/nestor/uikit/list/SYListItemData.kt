package com.nestor.uikit.list

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import java.util.UUID

data class SYListItemData(
    val label: String,
    val key: String = UUID.randomUUID().toString(),
    val subtitle: String? = null,
    val trailingIcon: SYListItemIcon? = null,
    val leadingIcon: SYListItemIcon? = null,
    val contextualActions: ContextualActionContainer? = null,
) {
    data class SYListItemIcon(
        val icon: Painter,
        val tint: Color,
        val foregroundTint: Color? = tint.copy(alpha = 0.05f),
    )

    data class ContextualActionContainer(
        val primary: ContextualAction,
        val secondary: ContextualAction? = null
    )

    data class ContextualAction(
        val icon: SYListItemIcon,
        val action: () -> Unit
    )
}