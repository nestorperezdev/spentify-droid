package com.nestor.expenses.data

import com.nestor.database.data.expense.ExpenseEntity
import com.nestor.schema.CreateExpenseMutation
import com.nestor.schema.type.ExpenseInput
import com.nestor.schema.utils.ResponseWrapper
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    suspend fun createExpense(expenseInput: ExpenseInput): ResponseWrapper<CreateExpenseMutation.Data>
    fun getExpenses(
        month: Int,
        year: Int,
        cursor: Int?,
        pageSize: Int?,
        userUid: String
    ): Flow<ResponseWrapper<List<ExpenseEntity>>>
}