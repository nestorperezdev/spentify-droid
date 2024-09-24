package com.nestor.charts.util

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect

@Composable
internal fun Line(dashed: Boolean = false) {
    val color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    val pathEffect = if (dashed) PathEffect.dashPathEffect(floatArrayOf(12f, 18f), 0f) else null
    Canvas(Modifier.fillMaxWidth()) {
        drawLine(
            color = color,
            start = Offset(20f, 0f),
            end = Offset(size.width - 20, 0f),
            strokeWidth = 1f,
            pathEffect = pathEffect
        )
    }
}