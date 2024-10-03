package com.nestor.uikit.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nestor.uikit.SpentifyTheme

@Preview
@Composable
fun SYBottomNavigationBarPreview() {
    val items = listOf(
        SYBottomNavBarData(
            "Home",
            androidx.core.R.drawable.ic_call_answer,
            "Home",
            0
        ),
        SYBottomNavBarData(
            "Expenses",
            androidx.core.R.drawable.ic_call_decline,
            "Expenses",
            1
        ),
        SYBottomNavBarData(
            "Monthly Report",
            androidx.core.R.drawable.ic_call_answer_video_low,
            "Monthly",
            2
        ),
        SYBottomNavBarData(
            "Account",
            androidx.core.R.drawable.ic_call_decline_low,
            "account",
            3
        ),
    )
    var activeItem by remember { mutableStateOf(items.first()) }
    SpentifyTheme {
        Scaffold(
            bottomBar = {
                SYBottomNavigationBar(
                    items = items,
                    currentActiveItem = activeItem,
                    onNavItemClicked = {
                        activeItem = it
                    }
                )
            }) {
            Box(modifier = Modifier.padding(it)) {
                Text(text = "Content")
            }
        }
    }
}