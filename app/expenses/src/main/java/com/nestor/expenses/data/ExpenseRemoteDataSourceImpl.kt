package com.nestor.expenses.data

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import com.nestor.schema.CreateExpenseMutation
import com.nestor.schema.ExpensesListQuery
import com.nestor.schema.type.ExpenseInput
import javax.inject.Inject

class ExpenseRemoteDataSourceImpl @Inject constructor(val client: ApolloClient) :
    ExpenseRemoteDataSource {
    override suspend fun createExpense(expenseInput: ExpenseInput): ApolloResponse<CreateExpenseMutation.Data> =
        client.mutation(CreateExpenseMutation(expenseInput)).execute()

    override suspend fun getExpenses(
        month: Int,
        year: Int,
        pageSize: Int?,
        pageNumber: Int
    ): ApolloResponse<ExpensesListQuery.Data> {
        return client.query(
            ExpensesListQuery(
                month = month,
                year = year,
                limit = Optional.presentIfNotNull(pageSize),
                page = pageNumber
            )
        ).execute()
    }
}