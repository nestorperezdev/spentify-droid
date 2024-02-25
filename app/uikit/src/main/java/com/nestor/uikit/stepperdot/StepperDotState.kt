package com.nestor.uikit.stepperdot

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class StepperDotState(val size: Int, currentDot: Int = 0) {
    private val _currentDotState: MutableStateFlow<Int> = MutableStateFlow(currentDot)
    val currentDotState = _currentDotState.asStateFlow()
    fun moveToDotNumber(dotNumber: Int) {
        if (dotNumber in 0..size - 1) {
            _currentDotState.update { dotNumber }
        }
    }

    fun nextDot() {
        if (_currentDotState.value < size - 1) {
            _currentDotState.update { it + 1 }
        }
    }

    fun isLastDot(): Boolean {
        return _currentDotState.value == size - 1
    }

    fun previousDot() {
        if (_currentDotState.value > 0) {
            _currentDotState.update { it - 1 }
        }
    }

    fun getCurrentDot(): Int {
        return _currentDotState.value
    }
}


@Composable
fun rememberStepperDotState(size: Int, currentDot: Int = 0): StepperDotState {
    return remember { StepperDotState(size, currentDot) }
}