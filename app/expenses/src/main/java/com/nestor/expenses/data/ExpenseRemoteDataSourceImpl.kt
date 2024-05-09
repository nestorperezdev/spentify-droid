package com.nestor.expenses.data

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.nestor.schema.CreateExpenseMutation
import com.nestor.schema.type.ExpenseInput
import javax.inject.Inject

class ExpenseRemoteDataSourceImpl @Inject constructor(val client: ApolloClient) :
    ExpenseRemoteDataSource {
    override suspend fun createExpense(expenseInput: ExpenseInput): ApolloResponse<CreateExpenseMutation.Data> =
        client.mutation(CreateExpenseMutation(expenseInput)).execute()
}