package com.nestor.expenses.data

import android.icu.util.Calendar
import com.nestor.database.data.expense.ExpenseDao
import com.nestor.database.data.expense.ExpenseEntity
import javax.inject.Inject

class ExpenseLocalDataSourceImpl @Inject constructor(private val expenseDao: ExpenseDao) : ExpenseLocalDataSource {
    override suspend fun saveExpenseList(expenseList: List<ExpenseEntity>) {
        expenseDao.upsertAll(expenseList)
    }

    override suspend fun getExpenses(
        month: Int,
        year: Int,
        afterCursor: Int,
        limit: Int,
        userUuid: String
    ): List<ExpenseEntity> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, month - 1)
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val date = calendar.time
        return expenseDao.getExpenses(date, date, afterCursor, userUuid, limit)
    }
}