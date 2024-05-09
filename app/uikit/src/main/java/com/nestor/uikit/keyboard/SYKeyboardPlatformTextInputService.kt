package com.nestor.uikit.keyboard

import android.util.Log
import androidx.compose.ui.text.input.EditCommand
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.ImeOptions
import androidx.compose.ui.text.input.PlatformTextInputService
import androidx.compose.ui.text.input.TextFieldValue

private const val TAG = "SYKeyboardPlatformTextI"
class SYKeyboardPlatformTextInputService : PlatformTextInputService {
    override fun hideSoftwareKeyboard() {
        //no-op
    }

    override fun showSoftwareKeyboard() {
        //no-op
    }

    override fun startInput(
        value: TextFieldValue,
        imeOptions: ImeOptions,
        onEditCommand: (List<EditCommand>) -> Unit,
        onImeActionPerformed: (ImeAction) -> Unit
    ) {
        Log.i(TAG, "startInput: $value")
    }

    override fun stopInput() {
        //no-op
    }

    override fun updateState(oldValue: TextFieldValue?, newValue: TextFieldValue) {
        Log.i(TAG, "updateState: $oldValue, $newValue")
    }
}