package com.nestor.expenses.data

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Optional
import com.nestor.schema.CreateExpenseMutation
import com.nestor.schema.DeleteExpenseMutation
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
        cursor: Int,
        currencyCode: String
    ): ApolloResponse<ExpensesListQuery.Data> {
        return client.query(
            ExpensesListQuery(
                month = month,
                year = year,
                limit = Optional.presentIfNotNull(pageSize),
                cursor = cursor,
                currencyCode = currencyCode
            )
        ).execute()
    }

    override suspend fun deleteExpense(id: String) {
        client.mutation(DeleteExpenseMutation(id)).execute()
    }
}