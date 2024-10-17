package com.nestor.expenses.ui.expenselist

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nestor.database.data.catergory.CategoryEntity
import com.nestor.database.data.expense.ExpenseEntity
import com.nestor.database.data.expensewithcategory.ExpenseWithCategoryEntity
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.theme.spacing.LocalSYPadding
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Date

@Preview
@Composable
fun ExpenseListContentPreview() {
    SpentifyTheme {
        Scaffold {
            ExpenseListContent(
                expenseListState = MutableStateFlow(
                    listOf(
                        ExpenseWithCategoryEntity(
                            expense = ExpenseEntity(
                                id = "1",
                                amount = 100.0,
                                date = Date(1729197805495),
                                cursor = 0,
                                storedAt = Date(1729197805495),
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
                                date = Date(1729197805495),
                                cursor = 0,
                                storedAt = Date(1729197805495),
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
                                subcategoryId = "1",
                                tint = 0
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