package com.nestor.spentify.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.nestor.account.ui.AccountScreen
import com.nestor.dashboard.ui.DashboardScreen
import com.nestor.expenses.ui.expenselist.ExpenseListScreen
import com.nestor.uikit.statusbar.SYStatusBar
import com.nestor.uikit.statusbar.StatusBarType
import com.nestor.uikit.theme.spacing.LocalSYPadding
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    statusBarType: StatusBarType?,
    onNewExpenseClick: () -> Unit = {}
) {
    val currentItem = remember { mutableStateOf(NavItems.Home) }
    val pagerState = rememberPagerState { 3 }
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(pagerState.currentPage) {
        currentItem.value = when (pagerState.currentPage) {
            0 -> NavItems.Home
            1 -> NavItems.Expenses
            2 -> NavItems.Account
            else -> NavItems.Home
        }
    }
    Scaffold(
        topBar = { statusBarType?.let { SYStatusBar(it) } },
        bottomBar = {
            BottomNavBar(selectedItem = currentItem, onNavItemClicked = {
                currentItem.value = it
                coroutineScope.launch {
                    pagerState.animateScrollToPage(it.ordinal)
                }
            })
        }) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false
        ) {
            when (it) {
                0 -> DashboardScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(LocalSYPadding.current.screenHorizontalPadding),
                    onNewExpenseClick = onNewExpenseClick
                )

                1 ->
                    ExpenseListScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(LocalSYPadding.current.screenHorizontalPadding)
                    )

                2 -> AccountScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(LocalSYPadding.current.screenHorizontalPadding)
                )
            }
        }
    }
}