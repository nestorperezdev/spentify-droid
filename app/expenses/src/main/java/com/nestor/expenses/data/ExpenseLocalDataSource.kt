package com.nestor.expenses.data

import com.nestor.database.data.expense.ExpenseEntity


interface ExpenseLocalDataSource {
    suspend fun saveExpenseList(expenseList: List<ExpenseEntity>)
    suspend fun getExpenses(
        month: Int,
        year: Int,
        afterCursor: Int,
        limit: Int,
        userUuid: String
    ): List<ExpenseEntity>
}
