package com.nestor.expenses.data.expensewithcategory

import com.nestor.database.data.expensewithcategory.ExpenseWithCategoryEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface ExpenseWithCategoryLocalDataSource {
    fun getExpensesWithCategory(
        month: Int,
        year: Int,
        userUuid: String,
        expirationDate: Date,
        currencyCode: String
    ): Flow<List<ExpenseWithCategoryEntity>>
}