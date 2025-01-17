package com.nestor.schema

import com.nestor.schema.ExpensesListQuery.Expense
import com.nestor.schema.ExpensesListQuery.ExpensesList
import com.nestor.schema.fragment.Category
import com.nestor.schema.fragment.CurrencyInfo
import com.nestor.schema.fragment.ExpenseFragment
import com.nestor.schema.fragment.ExpenseFragment.Currency
import com.nestor.schema.fragment.ExpenseFragment.SelectedCurrency
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.util.Date
import kotlin.collections.List

val list = ExpensesList(
    expenses = listOf(
        Expense(
            __typename = "ExpenseFragment",
            expenseFragment = ExpenseFragment(
                currency = Currency(
                    __typename = "Currency",
                    currencyInfo = CurrencyInfo(
                        code = "mx",
                        name = "Mexican Peso",
                        lastUpdate = "2022-01-01",
                        symbol = "$",
                        usdRate = 20.0,
                        exchangeId = "123"
                    )
                ),
                selectedCurrency = SelectedCurrency(
                    __typename = "SelectedCurrency",
                    currencyInfo = CurrencyInfo(
                        code = "mx",
                        name = "Mexican Peso",
                        lastUpdate = "2022-01-01",
                        symbol = "$",
                        usdRate = 20.0,
                        exchangeId = "123"
                    )
                ),
                description = "Description text",
                usdValue = 10.0,
                value = 100.5,
                cursor = 10,
                id = "123",
                date = Date(1732572704552),
                category = ExpenseFragment.Category(
                    __typename = "Category",
                    category = Category(
                        categoryName = "Food",
                        iconId = "icon id",
                        id = "123",
                        tint = null
                    )
                )
            )
        ), Expense(
            __typename = "ExpenseFragment",
            expenseFragment = ExpenseFragment(
                currency = Currency(
                    __typename = "Currency",
                    currencyInfo = CurrencyInfo(
                        code = "mx",
                        name = "Mexican Peso",
                        lastUpdate = "2022-01-01",
                        symbol = "$",
                        usdRate = 20.0,
                        exchangeId = "123"
                    )
                ),
                selectedCurrency = SelectedCurrency(
                    __typename = "SelectedCurrency",
                    currencyInfo = CurrencyInfo(
                        code = "mx",
                        name = "Mexican Peso",
                        lastUpdate = "2022-01-01",
                        symbol = "$",
                        usdRate = 20.0,
                        exchangeId = "123"
                    )
                ),
                description = "Description text",
                usdValue = 10.0,
                value = 100.5,
                cursor = 10,
                id = "123",
                date = Date(1732572704552),
                category = ExpenseFragment.Category(
                    __typename = "Category",
                    category = Category(
                        categoryName = "Food",
                        iconId = "icon id",
                        id = "123",
                        tint = null
                    )
                )
            )
        ), Expense(
            __typename = "ExpenseFragment",
            expenseFragment = ExpenseFragment(
                currency = Currency(
                    __typename = "Currency",
                    currencyInfo = CurrencyInfo(
                        code = "mx",
                        name = "Mexican Peso",
                        lastUpdate = "2022-01-01",
                        symbol = "$",
                        usdRate = 20.0,
                        exchangeId = "123"
                    )
                ),
                selectedCurrency = SelectedCurrency(
                    __typename = "SelectedCurrency",
                    currencyInfo = CurrencyInfo(
                        code = "mx",
                        name = "Mexican Peso",
                        lastUpdate = "2022-01-01",
                        symbol = "$",
                        usdRate = 20.0,
                        exchangeId = "123"
                    )
                ),
                description = "Description text",
                usdValue = 10.0,
                value = 100.5,
                cursor = 10,
                id = "123",
                date = Date(1732572704552),
                category = ExpenseFragment.Category(
                    __typename = "Category",
                    category = Category(
                        categoryName = "Food",
                        iconId = "icon id",
                        id = "123",
                        tint = null
                    )
                )
            )
        )
    ),
    totalItems = 10
)

class DataCreationTest {
    fun generateData(): ExpensesList {
        return ExpensesList(
            totalItems = 10,
            expenses = List(3) {
                Expense(
                    __typename = "ExpenseFragment",
                    expenseFragment = ExpenseFragment(
                        currency = Currency(
                            __typename = "Currency",
                            currencyInfo = CurrencyInfo(
                                code = "mx",
                                name = "Mexican Peso",
                                lastUpdate = "2022-01-01",
                                symbol = "$",
                                usdRate = 20.0,
                                exchangeId = "123"
                            )
                        ),
                        description = "Description text",
                        id = "123",
                        date = Date(),
                        value = 100.50,
                        cursor = 10,
                        usdValue = 10.0,
                        category = ExpenseFragment.Category(
                            __typename = "Category",
                            category = Category(
                                categoryName = "Food",
                                id = "123",
                                tint = null,
                                iconId = "icon\n id"
                            )
                        ),
                        selectedCurrency = SelectedCurrency(
                            __typename = "SelectedCurrency",
                            currencyInfo = CurrencyInfo(
                                code = "mx",
                                name = "Mexican Peso",
                                lastUpdate = "2022-01-01",
                                symbol = "$",
                                usdRate = 20.0,
                                exchangeId = "123"
                            )
                        )
                    )
                )
            }
        )
    }

    @Test
    fun debug() {
        val data = generateData()
        println(getStringInstanceOfData(data))
        //assertEquals(data, list)
    }
}