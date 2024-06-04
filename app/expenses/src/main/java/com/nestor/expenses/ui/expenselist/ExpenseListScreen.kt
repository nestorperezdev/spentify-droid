package com.nestor.expenses.ui.expenselist

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import com.nestor.common.util.formatWithDayAndDate
import com.nestor.dashboard.R
import com.nestor.database.data.catergory.CategoryEntity
import com.nestor.database.data.expense.ExpenseEntity
import com.nestor.database.data.expensewithcategory.ExpenseWithCategoryEntity
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.list.DragDirection
import com.nestor.uikit.list.SYListItem
import com.nestor.uikit.list.SYListItemData
import com.nestor.uikit.list.isScrolledToEnd
import com.nestor.uikit.theme.image.LocalSYImageServerProvider
import com.nestor.uikit.theme.spacing.LocalSYPadding
import com.nestor.uikit.util.formatMoneyAmount
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Date

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
private fun ExpenseListContent(
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
private const val TAG = "ExpenseListScreen"

@Composable
private fun generateCategoryItem(category: CategoryEntity?): SYListItemData.SYListItemIcon? {
    val tint = MaterialTheme.colorScheme.primary
    val baseUrl = LocalSYImageServerProvider.current
    val painter = rememberAsyncImagePainter(
        model = category?.iconUrl(baseUrl)
    )
    return remember(category?.id ?: "null") {
        Log.i(TAG, "generateCategoryItem: ${category?.iconUrl(baseUrl)}")
        category?.let {
            SYListItemData.SYListItemIcon(
                icon = painter,
                tint = tint,
            )
        } ?: run { null }
    }
}

@Preview
@Composable
private fun ExpenseListContentPreview() {
    SpentifyTheme {
        Scaffold {
            ExpenseListContent(
                expenseListState = MutableStateFlow(
                    listOf(
                        ExpenseWithCategoryEntity(
                            expense = ExpenseEntity(
                                id = "1",
                                amount = 100.0,
                                date = Date(),
                                cursor = 0,
                                storedAt = Date(),
                                currencyCode = "EUR",
                                userUuid = "",
                                usdValue = 100.0,
                                categoryId = "",
                                description = "Un burrito"
                            ),
                            category = null
                        ),
                        ExpenseWithCategoryEntity(
                            expense = ExpenseEntity(
                                id = "2",
                                amount = 3500.0,
                                date = Date(),
                                cursor = 0,
                                storedAt = Date(),
                                currencyCode = "EUR",
                                userUuid = "",
                                usdValue = 100.0,
                                categoryId = "",
                                description = ""
                            ),
                            category = CategoryEntity(
                                id = "1",
                                name = "Categoria",
                                icon = "icon",
                                subcategoryId = "1"
                            )
                        )
                    )
                ),
                userCurrencySymbolState = MutableStateFlow("€"),
                isLoadingState = MutableStateFlow(false),
                isEndReachedState = MutableStateFlow(true),
                modifier = Modifier
                    .padding(it)
                    .padding(LocalSYPadding.current.screenHorizontalPadding),
            )
        }
    }
}