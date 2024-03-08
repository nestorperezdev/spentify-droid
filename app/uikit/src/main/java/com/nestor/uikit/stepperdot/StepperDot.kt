package com.nestor.uikit.stepperdot

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.button.SYButton

@Composable
fun SYStepperDot(
    modifier: Modifier = Modifier,
    state: StepperDotState,
    dotColor: Color = MaterialTheme.colorScheme.primary
) {
    Row(modifier = modifier, horizontalArrangement = spacedBy(10.dp)) {
        repeat(state.size) { index ->
            Dot(
                active = index == state.currentDotState.collectAsState().value,
                dotColor = dotColor
            )
        }
    }
}

@Composable
private fun Dot(
    modifier: Modifier = Modifier,
    active: Boolean,
    dotColor: Color
) {
    val color by animateColorAsState(
        targetValue = if (active) dotColor else MaterialTheme.colorScheme.scrim,
        label = "Dot color animation",
        animationSpec = tween(300)
    )
    val width by animateDpAsState(
        targetValue = if (active) 34.dp else 12.dp,
        label = "Dot width animation",
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow)
    )
    Box(
        modifier = modifier
            .size(height = 12.dp, width = width)
            .clip(RoundedCornerShape(12.dp))
            .background(color = color)
    )
}

@Preview
@Composable
fun StepperDotPreview() {
    SpentifyTheme {
        Scaffold {
            Column(
                modifier = Modifier
                    .padding(it)
                    .padding(24.dp),
                verticalArrangement = spacedBy(24.dp)
            ) {
                val state = rememberStepperDotState(5)
                SYButton(
                    onClick = { state.moveToDotNumber(state.getCurrentDot() + 1) },
                    buttonText = "Move to the next item"
                )
                SYStepperDot(state = state)
            }
        }
    }
}