package com.nestor.expenses.data

import com.nestor.database.data.expense.ExpenseEntity
import com.nestor.database.data.expense.ExpenseWithCategoryAndSubcategory
import com.nestor.database.data.expensewithcategory.ExpenseWithCategoryEntity
import com.nestor.schema.CreateExpenseMutation
import com.nestor.schema.type.ExpenseInput
import com.nestor.schema.utils.ResponseWrapper
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface ExpenseRepository {
    suspend fun createExpense(expenseInput: ExpenseInput): ResponseWrapper<CreateExpenseMutation.Data>
    fun getExpenses(baseMonthDate: Date): Flow<List<ExpenseWithCategoryEntity>>
    fun getExpensesWithCategoryAndSubcategory(baseMonthDate: Date): Flow<List<ExpenseWithCategoryAndSubcategory>>

    /**
     * Int value will be the amount of values fetched from the api
     */
    suspend fun fetchMoreExpenses(
        cursor: Int,
        pageSize: Int,
        userUid: String,
        year: Int,
        month: Int,
        currencyCode: String
    ): Int

    suspend fun saveExpenses(expense: ExpenseEntity)
    suspend fun deleteExpense(expense: ExpenseEntity)
}