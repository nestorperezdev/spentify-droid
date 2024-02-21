package com.nestor.login

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nestor.uikit.SpentifyTheme

@Composable
fun LoginScreen() {
    LoginScreenContent()
}

@Composable
private fun LoginScreenContent() {
    SpentifyTheme {
        Scaffold {
            Text("Login Screen", modifier = Modifier.padding(it))
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreenContent()
}
