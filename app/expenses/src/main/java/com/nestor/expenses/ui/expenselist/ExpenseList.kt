package com.nestor.expenses.ui.expenselist

import com.nestor.database.data.expense.ExpenseEntity

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
