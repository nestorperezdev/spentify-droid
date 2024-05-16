package com.nestor.expenses.ui.expenselist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nestor.common.util.formatWithDayAndDate
import com.nestor.dashboard.R
import com.nestor.schema.utils.ResponseWrapper
import com.nestor.uikit.list.SYListItem
import com.nestor.uikit.list.SYListItemData
import com.nestor.uikit.list.isScrolledToEnd
import com.nestor.uikit.util.formatMoneyAmount
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ExpenseListScreen(
    modifier: Modifier = Modifier,
    viewModel: ExpenseListViewModel = hiltViewModel()
) {
    ExpenseListContent(
        modifier = modifier,
        expenseListState = viewModel.expenseItems,
        userCurrencySymbolState = viewModel.userCurrencySymbol,
        onScrollEndReached = viewModel::onScrollEndReached
    )
}

@Composable
private fun ExpenseListContent(
    modifier: Modifier = Modifier,
    expenseListState: StateFlow<ResponseWrapper<ExpenseList>>,
    userCurrencySymbolState: StateFlow<String>,
    onScrollEndReached: () -> Unit = {}
) {
    val expenseList by expenseListState.collectAsState()
    val currencySymbol by userCurrencySymbolState.collectAsState()
    val scrollState = rememberLazyListState()
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        state = scrollState
    ) {
        expenseList.body?.let {
            items(it.items) { item ->
                val separator = if (item.description.isEmpty()) "" else " ⦁ "
                SYListItem(
                    item = SYListItemData(
                        label = stringResource(
                            R.string.currency_format,
                            currencySymbol,
                            item.usdValue.formatMoneyAmount()
                        ),
                        subtitle = "${item.date.formatWithDayAndDate()}${separator}${item.description}"
                    )
                )
            }
        }
        if (expenseList.isLoading) {
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
    val endOfListReached by remember {
        derivedStateOf {
            scrollState.isScrolledToEnd()
        }
    }
    LaunchedEffect(endOfListReached) {
        if (endOfListReached) {
            onScrollEndReached()
        }
    }
}