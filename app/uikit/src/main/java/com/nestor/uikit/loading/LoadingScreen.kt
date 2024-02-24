package com.nestor.uikit.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nestor.uikit.SpentifyTheme

@Composable
fun LoadingScreen(modifier: Modifier = Modifier, text: String? = null) {
    Scaffold {
        Column(
            modifier = modifier
                .padding(it)
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CircularProgressIndicator()
            text?.let {
                Spacer(modifier = Modifier.height(28.dp))
                Text(text)
            }
        }
    }
}

@Preview
@Composable
fun LoadingScreenPreview() {
    SpentifyTheme {
        LoadingScreen()
    }
}