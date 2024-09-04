package com.nestor.expenses.data

import com.nestor.database.data.expense.ExpenseEntity
import com.nestor.database.data.expensewithcategory.ExpenseWithCategoryEntity
import com.nestor.schema.CreateExpenseMutation
import com.nestor.schema.type.ExpenseInput
import com.nestor.schema.utils.ResponseWrapper
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    suspend fun createExpense(expenseInput: ExpenseInput): ResponseWrapper<CreateExpenseMutation.Data>
    fun getExpenses(
        month: Int,
        year: Int,
        userUid: String,
        currencyCode: String
    ): Flow<List<ExpenseWithCategoryEntity>>

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