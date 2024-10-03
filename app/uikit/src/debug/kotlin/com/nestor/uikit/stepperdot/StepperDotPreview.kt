package com.nestor.uikit.stepperdot

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.button.SYButton

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