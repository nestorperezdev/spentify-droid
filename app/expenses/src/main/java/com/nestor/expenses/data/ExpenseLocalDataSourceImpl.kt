package com.nestor.expenses.data

import android.icu.util.Calendar
import com.nestor.database.data.expense.ExpenseDao
import com.nestor.database.data.expense.ExpenseEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

class ExpenseLocalDataSourceImpl @Inject constructor(private val expenseDao: ExpenseDao) :
    ExpenseLocalDataSource {
    override suspend fun saveExpenseList(expenseList: List<ExpenseEntity>) {
        expenseDao.upsertAll(expenseList)
    }

    override fun getExpenses(
        month: Int,
        year: Int,
        userUuid: String,
        expirationDate: Date,
        currencyCode: String,
    ): Flow<List<ExpenseEntity>> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, month - 1)
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val firstDate = calendar.time
        calendar.set(Calendar.MONTH, month)
        calendar.add(Calendar.DATE, -1)
        val lastDate = calendar.time
        return expenseDao.getExpenses(
            from = firstDate,
            to = lastDate,
            userUuid = userUuid,
            expirationDate = expirationDate,
            currencyCode = currencyCode
        )
    }

    override suspend fun deleteExpense(expense: ExpenseEntity) {
        expenseDao.delete(expense)
    }
}