package com.nestor.uikit.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nestor.uikit.SpentifyTheme

@Composable
fun SYBottomNavigationBar(
    modifier: Modifier = Modifier,
    items: List<SYBottomNavBarData>,
    currentActiveItem: SYBottomNavBarData,
    onNavItemClicked: (SYBottomNavBarData) -> Unit
) {
    BottomAppBar(
        actions = {
            items.forEach { item ->
                SYBottomNavBarItem(
                    item = item,
                    isSelected = item == currentActiveItem,
                    onNavItemClicked = onNavItemClicked,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
        },
        modifier = modifier.shadow(8.dp),
        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = 0.dp
    )
}

@Composable
private fun SYBottomNavBarItem(
    modifier: Modifier = Modifier,
    item: SYBottomNavBarData,
    isSelected: Boolean,
    onNavItemClicked: (SYBottomNavBarData) -> Unit
) {
    val animatedColor =
        animateColorAsState(targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface)
    Column(
        modifier = modifier.clickable { onNavItemClicked(item) },
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(item.iconResource),
            contentDescription = null,
            tint = animatedColor.value
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = item.text, style = MaterialTheme.typography.labelMedium,
            color = animatedColor.value,
        )
    }
}

@Preview
@Composable
private fun SYBottomNavigationBarPreview() {
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