package com.nestor.expenses.data

import com.nestor.schema.CreateExpenseMutation
import com.nestor.schema.type.ExpenseDetail
import com.nestor.schema.type.ExpenseInput
import com.nestor.schema.utils.ResponseWrapper

interface ExpenseRepository {
    suspend fun createExpense(expenseInput: ExpenseInput): ResponseWrapper<CreateExpenseMutation.Data>
}