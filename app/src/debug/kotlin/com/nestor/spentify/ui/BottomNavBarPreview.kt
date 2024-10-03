package com.nestor.spentify.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nestor.uikit.SpentifyTheme

@Preview
@Composable
private fun BottomNavBarPreview() {
    SpentifyTheme {
        Scaffold(bottomBar = { BottomNavBar() }) {
            Box(modifier = Modifier.padding(it))
        }
    }
}