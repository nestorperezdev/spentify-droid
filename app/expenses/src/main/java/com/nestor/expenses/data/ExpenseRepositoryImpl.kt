package com.nestor.expenses.data

import com.nestor.schema.type.ExpenseInput
import com.nestor.schema.utils.safeApiCall
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor(private val remoteDataSource: ExpenseRemoteDataSource) :
    ExpenseRepository {
    override suspend fun createExpense(expenseInput: ExpenseInput) =
        safeApiCall { remoteDataSource.createExpense(expenseInput) }
}