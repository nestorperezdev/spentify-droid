package com.nestor.expenses.data

import com.apollographql.apollo3.api.ApolloResponse
import com.nestor.schema.CreateExpenseMutation
import com.nestor.schema.ExpensesListQuery
import com.nestor.schema.type.ExpenseInput

interface ExpenseRemoteDataSource {
    suspend fun createExpense(expenseInput: ExpenseInput): ApolloResponse<CreateExpenseMutation.Data>
    suspend fun getExpenses(month: Int, year: Int, pageSize: Int?, cursor: Int?): ApolloResponse<ExpensesListQuery.Data>
}