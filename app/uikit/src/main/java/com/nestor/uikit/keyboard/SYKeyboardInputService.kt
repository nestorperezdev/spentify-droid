package com.nestor.uikit.keyboard

import androidx.compose.ui.text.input.PlatformTextInputService
import androidx.compose.ui.text.input.TextInputService

open class SYKeyboardInputService(private val platformTextInputService: PlatformTextInputService = SYKeyboardPlatformTextInputService()) :
    TextInputService(platformTextInputService)