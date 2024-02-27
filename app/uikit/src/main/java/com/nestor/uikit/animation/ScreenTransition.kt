package com.nestor.uikit.animation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.ui.unit.IntOffset

val syEnterTransition: EnterTransition = slideIn(tween(250), { IntOffset(it.width, 0) })
val syPopEnterTransition: EnterTransition = slideIn(tween(250), { IntOffset(-it.width, 0) })
val syExitTransition: ExitTransition = slideOut(tween(250), { IntOffset(-it.width, 0) })
val syPopExitTransition: ExitTransition = slideOut(tween(250), { IntOffset(it.width, 0) })