package com.nestor.uikit.keyboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.tooling.preview.Preview
import com.nestor.uikit.SpentifyTheme

@Preview
@Composable
private fun SYKeyboardPreview() {
    var keyPressed by remember {
        mutableStateOf<Key?>(null)
    }
    SpentifyTheme {
        Scaffold {
            Column(modifier = Modifier.padding(it)) {
                Text(text = "Keypressed:")
                keyPressed?.let {
                    Text(text = "${it.keyCode}")
                }
                Spacer(modifier = Modifier.weight(1f))
                SYKeyboard(modifier = Modifier.fillMaxWidth()) {
                    keyPressed = it
                }
            }
        }
    }
}