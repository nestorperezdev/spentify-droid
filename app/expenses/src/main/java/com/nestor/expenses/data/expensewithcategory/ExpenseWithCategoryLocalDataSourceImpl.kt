package com.nestor.expenses.data.expensewithcategory

import android.icu.util.Calendar
import com.nestor.database.data.expense.ExpenseDao
import com.nestor.database.data.expensewithcategory.ExpenseWithCategoryDao
import com.nestor.database.data.expensewithcategory.ExpenseWithCategoryEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

class ExpenseWithCategoryLocalDataSourceImpl @Inject constructor(
    private val localDataSource: ExpenseWithCategoryDao
) : ExpenseWithCategoryLocalDataSource {
    override fun getExpensesWithCategory(
        month: Int,
        year: Int,
        userUuid: String,
        expirationDate: Date,
        currencyCode: String
    ): Flow<List<ExpenseWithCategoryEntity>> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, month - 1)
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val firstDate = calendar.time
        calendar.set(Calendar.MONTH, month)
        calendar.add(Calendar.DATE, -1)
        val lastDate = calendar.time
        return localDataSource.getExpensesWithCategory(
            from = firstDate,
            to = lastDate,
            userUuid = userUuid,
            expirationDate = expirationDate,
            currencyCode = currencyCode
        )
    }
}