package com.nestor.expenses.ui.expenselist

import com.nestor.database.data.expense.ExpenseEntity

data class ExpenseList(
    /**
     * When local fetching we don't know the total pages and total items
     */
    val totalItems: Int? = null,
    val totalPages: Int? = null,
    val items: List<ExpenseEntity>,
    val endReached: Boolean = false
) {
    infix fun mergeForward(newList: ExpenseList): ExpenseList {
        return ExpenseList(totalItems = newList.totalItems, items = this.items + newList.items)
    }

    infix fun mergeBackwards(newList: ExpenseList): ExpenseList {
        return ExpenseList(totalItems = newList.totalItems, items = newList.items + this.items)
    }
}
