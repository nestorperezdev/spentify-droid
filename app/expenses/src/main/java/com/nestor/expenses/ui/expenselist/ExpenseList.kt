package com.nestor.expenses.ui.expenselist

import com.nestor.expenses.data.model.ExpenseEntity
import com.nestor.schema.ExpensesListQuery
import java.util.Date

data class ExpenseList(
    val totalItems: Int,
    val items: List<ExpenseEntity>,
    val endReached: Boolean = false
) {
    fun firstItemCursor() = items.firstOrNull()?.cursor

    infix fun mergeForward(newList: ExpenseList): ExpenseList {
        return ExpenseList(totalItems = newList.totalItems, items = this.items + newList.items)
    }

    infix fun mergeBackwards(newList: ExpenseList): ExpenseList {
        return ExpenseList(totalItems = newList.totalItems, items = newList.items + this.items)
    }
}

internal fun ExpensesListQuery.ExpensesList.toExpenseList(): ExpenseList {
    return ExpenseList(
        totalItems = this.totalItems,
        items = this.expenses.map {
            ExpenseEntity(
                id = it.expenseFragment.id,
                description = it.expenseFragment.description,
                cursor = it.expenseFragment.cursor,
                usdValue = it.expenseFragment.usdValue,
                amount = it.expenseFragment.value,
                //  TODO: Handle date
                date = Date()
            )
        })
}