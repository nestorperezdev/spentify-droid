package com.nestor.expenses.data

import com.nestor.expenses.data.model.ExpenseEntity
import com.nestor.expenses.ui.expenselist.ExpenseList
import com.nestor.schema.CreateExpenseMutation
import com.nestor.schema.ExpensesListQuery
import com.nestor.schema.type.ExpenseInput
import com.nestor.schema.utils.ResponseWrapper
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    suspend fun createExpense(expenseInput: ExpenseInput): ResponseWrapper<CreateExpenseMutation.Data>
    suspend fun getExpenses(
        month: Int,
        year: Int,
        cursor: Int?,
        pageSize: Int?
    ): ResponseWrapper<ExpensesListQuery.Data>
    fun getExpensesList(): Flow<ResponseWrapper<ExpenseList>>
}