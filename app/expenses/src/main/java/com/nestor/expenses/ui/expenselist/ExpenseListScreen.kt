package com.nestor.expenses.ui.expenselist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import com.nestor.common.util.formatWithDayAndDate
import com.nestor.dashboard.R
import com.nestor.database.data.catergory.CategoryEntity
import com.nestor.database.data.expense.ExpenseEntity
import com.nestor.database.data.expensewithcategory.ExpenseWithCategoryEntity
import com.nestor.uikit.list.DragDirection
import com.nestor.uikit.list.SYListItem
import com.nestor.uikit.list.SYListItemData
import com.nestor.uikit.list.isScrolledToEnd
import com.nestor.uikit.theme.image.LocalSYImageServerProvider
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
        onScrollEndReached = viewModel::onScrollEndReached,
        isLoadingState = viewModel.isLoading,
        isEndReachedState = viewModel.isEndOfList,
        onDeleteAction = viewModel::onDeleteExpense,
        onEditAction = viewModel::onEditExpense
    )
}

@Composable
internal fun ExpenseListContent(
    modifier: Modifier = Modifier,
    expenseListState: StateFlow<List<ExpenseWithCategoryEntity>>,
    userCurrencySymbolState: StateFlow<String>,
    isLoadingState: StateFlow<Boolean>,
    isEndReachedState: StateFlow<Boolean>,
    onScrollEndReached: () -> Unit = {},
    onDeleteAction: (ExpenseEntity) -> Unit = {},
    onEditAction: (ExpenseEntity) -> Unit = {},
) {
    val expenseList by expenseListState.collectAsState()
    val currencySymbol by userCurrencySymbolState.collectAsState()
    val scrollState = rememberLazyListState()
    val isLoading by isLoadingState.collectAsState()
    val isEndReached by isEndReachedState.collectAsState()
    val deleteIcon = painterResource(id = com.nestor.uikit.R.drawable.baseline_delete_24)
    val deleteBackground = MaterialTheme.colorScheme.error
    val contextualActions = remember {
        SYListItemData.ContextualActionContainer(
            primary = SYListItemData.ContextualAction(
                action = {},
                icon = SYListItemData.SYListItemIcon(
                    icon = deleteIcon,
                    tint = deleteBackground,
                    foregroundTint = deleteBackground.copy(alpha = 0.5f),
                )
            ),
        )
    }

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        state = scrollState
    ) {
        items(expenseList, key = { it.expense.id }) { item ->
            ExpenseItem(
                item = item,
                actions = contextualActions,
                onDeleteAction = onDeleteAction,
                onEditAction = onEditAction,
                currencySymbol = currencySymbol
            )
        }
        if (isLoading) {
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
        if (isEndReached) {
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(com.nestor.expenses.R.string.end_of_list),
                        style = MaterialTheme.typography.labelMedium
                    )
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
        if (endOfListReached && !isLoading) {
            onScrollEndReached()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.ExpenseItem(
    modifier: Modifier = Modifier,
    item: ExpenseWithCategoryEntity,
    actions: SYListItemData.ContextualActionContainer,
    currencySymbol: String,
    onDeleteAction: (ExpenseEntity) -> Unit = {},
    onEditAction: (ExpenseEntity) -> Unit = {},
) {
    val separator = if (item.expense.description.isEmpty()) "" else " ⦁ "
    val leadingIcon = generateCategoryItem(item.category)
    SYListItem(
        item = SYListItemData(
            label = stringResource(
                R.string.currency_format,
                currencySymbol,
                item.expense.amount.formatMoneyAmount()
            ),
            subtitle = "${item.expense.date.formatWithDayAndDate()}${separator}${item.expense.description}",
            contextualActions = actions,
            leadingIcon = leadingIcon,
        ),
        onDragged = {
            if (it == DragDirection.LEFT) {
                onDeleteAction(item.expense)
            } else {
                onEditAction(item.expense)
            }
            0f
        },
        modifier = modifier.animateItemPlacement()
    )
}

@Composable
private fun generateCategoryItem(category: CategoryEntity?): SYListItemData.SYListItemIcon? {
    return category?.let {
        SYListItemData.SYListItemIcon(
            icon = rememberAsyncImagePainter(
                model = it.iconUrl(LocalSYImageServerProvider.current),
                imageLoader = LocalContext.current.imageLoader
            ),
            tint = it.tint?.let { tintInt -> Color(tintInt) } ?: MaterialTheme.colorScheme.primary,
        )
    }
}
