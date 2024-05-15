package com.nestor.expenses.data

import com.nestor.database.data.expense.ExpenseEntity
import com.nestor.schema.ExpensesListQuery
import com.nestor.schema.type.ExpenseInput
import com.nestor.schema.utils.ResponseWrapper
import com.nestor.schema.utils.safeApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Date
import javax.inject.Inject

private fun ExpensesListQuery.Expense.toEntity(
    userUid: String,
    storedAt: Date
): ExpenseEntity {
    return with(expenseFragment) {
        ExpenseEntity(
            userUuid = userUid,
            description = description,
            date = date,
            storedAt = storedAt,
            amount = value,
            cursor = cursor,
            usdValue = usdValue,
            id = id
        )
    }
}

class ExpenseRepositoryImpl @Inject constructor(
    private val remoteDataSource: ExpenseRemoteDataSource,
    private val localDataSource: ExpenseLocalDataSource
) : ExpenseRepository {

    override suspend fun createExpense(expenseInput: ExpenseInput) =
        safeApiCall { remoteDataSource.createExpense(expenseInput) }

    override fun getExpenses(
        month: Int,
        year: Int,
        cursor: Int?,
        pageSize: Int?,
        userUid: String
    ): Flow<ResponseWrapper<List<ExpenseEntity>>> = flow {
        val items = localDataSource.getExpenses(
            month = month,
            year = year,
            afterCursor = cursor ?: 0,
            limit = pageSize ?: 20,
            userUuid = userUid
        )
        if (items.isEmpty()) {
            emit(ResponseWrapper.loading())
            val remoteItems = remoteDataSource.getExpenses(
                month = month, year = year, cursor = cursor, pageSize = pageSize
            )
            val storedAt = Date()
            val itemsEntities: List<ExpenseEntity>? =
                remoteItems.data?.expensesList?.expenses?.map {
                    it.toEntity(
                        userUid = userUid,
                        storedAt = storedAt
                    )
                }
            itemsEntities?.let {
                localDataSource.saveExpenseList(itemsEntities)
                emit(ResponseWrapper.success(itemsEntities))
            }
        } else {
            emit(ResponseWrapper.success(items))
        }
    }

    /*safeApiCall {
        _expensesList.value = ResponseWrapper.loading(_expensesList.value.body)
        remoteDataSource.getExpenses(
            month = month, year = year, cursor = cursor, pageSize = pageSize
        )
    }.also {
        handleExpenseListResult(it)
    }*/

    /*private fun handleExpenseListResult(responseWrapper: ResponseWrapper<ExpensesListQuery.Data>) {
        responseWrapper.body?.let { updateExpensesList(it) }
        responseWrapper.error?.let { handleExpenseListError(it) }
    }*/

    /*private fun updateExpensesList(data: ExpensesListQuery.Data) {
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
    }*/
}
