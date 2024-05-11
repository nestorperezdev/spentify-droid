package com.nestor.expenses.data

import com.nestor.expenses.ui.expenselist.ExpenseList
import com.nestor.expenses.ui.expenselist.toExpenseList
import com.nestor.schema.ExpensesListQuery
import com.nestor.schema.type.ExpenseInput
import com.nestor.schema.utils.ResponseWrapper
import com.nestor.schema.utils.safeApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor(private val remoteDataSource: ExpenseRemoteDataSource) :
    ExpenseRepository {
    private val _expensesList =
        MutableStateFlow<ResponseWrapper<ExpenseList>>(ResponseWrapper.loading())

    override suspend fun createExpense(expenseInput: ExpenseInput) =
        safeApiCall { remoteDataSource.createExpense(expenseInput) }

    override suspend fun getExpenses(
        month: Int, year: Int, cursor: Int?, pageSize: Int?
    ): ResponseWrapper<ExpensesListQuery.Data> = safeApiCall {
        _expensesList.value = ResponseWrapper.loading(_expensesList.value.body)
        remoteDataSource.getExpenses(
            month = month, year = year, cursor = cursor, pageSize = pageSize
        )
    }.also {
        handleExpenseListResult(it)
    }

    private fun handleExpenseListResult(responseWrapper: ResponseWrapper<ExpensesListQuery.Data>) {
        responseWrapper.body?.let { updateExpensesList(it) }
        responseWrapper.error?.let { handleExpenseListError(it) }
    }

    private fun updateExpensesList(data: ExpensesListQuery.Data) {
        val lastResult =
            _expensesList.value.body ?: ExpenseList(data.expensesList.totalItems, emptyList())
        val currentResult = data.expensesList.toExpenseList()
        val responseFirstItemCursor = currentResult.firstItemCursor() ?: 0
        val currentFirstItemCursor = lastResult.firstItemCursor() ?: 0
        if (responseFirstItemCursor > currentFirstItemCursor) {
            //  it means pagination is forward.
            _expensesList.update { wrapper ->
                wrapper.copy(
                    body = lastResult mergeForward currentResult,
                    isLoading = false,
                    error = null
                )
            }
        } else if (responseFirstItemCursor < currentFirstItemCursor) {
            //  it means pagination is backwards.
            _expensesList.update { wrapper ->
                wrapper.copy(
                    body = lastResult mergeBackwards currentResult,
                    isLoading = false,
                    error = null
                )
            }
        } else {
            // end pagination reached
            _expensesList.update { wrapper ->
                wrapper.copy(
                    body = currentResult.copy(endReached = true),
                    isLoading = false,
                    error = null
                )
            }
        }
    }

    private fun handleExpenseListError(error: String) {
        _expensesList.value = ResponseWrapper.error(error, _expensesList.value.body)
    }

    override fun getExpensesList(): Flow<ResponseWrapper<ExpenseList>> {
        return _expensesList
    }
}
