package com.nestor.expenses.data

import com.nestor.database.data.expense.ExpenseEntity
import java.util.Date


interface ExpenseLocalDataSource {
    suspend fun saveExpenseList(expenseList: List<ExpenseEntity>)
    suspend fun getExpenses(
        month: Int,
        year: Int,
        page: Int,
        limit: Int,
        userUuid: String,
        expirationDate: Date
    ): List<ExpenseEntity>
}
