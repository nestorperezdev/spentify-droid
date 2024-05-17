package com.nestor.expenses.data

import com.nestor.database.data.expense.ExpenseEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date


interface ExpenseLocalDataSource {
    suspend fun saveExpenseList(expenseList: List<ExpenseEntity>)
    fun getExpenses(
        month: Int,
        year: Int,
        userUuid: String,
        expirationDate: Date,
        currencyCode: String
    ): Flow<List<ExpenseEntity>>
}
