package com.nestor.spentify.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nestor.spentify.R
import com.nestor.spentify.navigation.AppNavigationGraph
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.navigation.SYBottomNavBarData
import com.nestor.uikit.navigation.SYBottomNavigationBar

object NavItems {
    val Home = SYBottomNavBarData(
        iconResource = R.drawable.baseline_home_24,
        text = "Home",
        route = AppNavigationGraph.Home.route,
        ordinal = 0
    )
    val Expenses = SYBottomNavBarData(
        iconResource = R.drawable.baseline_receipt_long_24,
        text = "Expenses",
        route = "",
        ordinal = 1
    )
    val Account = SYBottomNavBarData(
        iconResource = R.drawable.baseline_manage_accounts_24,
        text = "Account",
        route = "",
        ordinal = 2
    )
}

val itemList = listOf(NavItems.Home, NavItems.Expenses, NavItems.Account)

@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    onNavItemClicked: (SYBottomNavBarData) -> Unit = {},
    selectedItem: MutableState<SYBottomNavBarData> = remember { mutableStateOf(NavItems.Home) }
) {
    SYBottomNavigationBar(
        items = itemList,
        onNavItemClicked = {
            onNavItemClicked(it)
            selectedItem.value = it
        },
        currentActiveItem = selectedItem.value,
        modifier = modifier
    )
}

@Preview
@Composable
private fun BottomNavBarPreview() {
    SpentifyTheme {
        Scaffold(bottomBar = { BottomNavBar() }) {
            Box(modifier = Modifier.padding(it))
        }
    }
}