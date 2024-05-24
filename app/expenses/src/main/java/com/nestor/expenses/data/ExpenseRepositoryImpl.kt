package com.nestor.expenses.data

import com.nestor.database.data.expense.ExpenseEntity
import com.nestor.schema.fragment.ExpenseFragment
import com.nestor.schema.type.ExpenseInput
import com.nestor.schema.utils.safeApiCall
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

fun ExpenseFragment.toEntity(
    userUid: String,
    storedAt: Date = Date()
): ExpenseEntity {
    return ExpenseEntity(
            userUuid = userUid,
            description = description,
            date = date,
            storedAt = storedAt,
            amount = value,
            cursor = cursor,
            usdValue = usdValue,
            id = id,
            currencyCode = selectedCurrency.currencyInfo.code
        )
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
        userUid: String,
        currencyCode: String
    ): Flow<List<ExpenseEntity>> {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DATE, -1)
        return localDataSource.getExpenses(
            month = month,
            year = year,
            userUuid = userUid,
            expirationDate = calendar.time,
            currencyCode = currencyCode
        )
    }

    override suspend fun fetchMoreExpenses(
        cursor: Int,
        pageSize: Int,
        userUid: String,
        year: Int,
        month: Int,
        currencyCode: String
    ): Int {
        val response = safeApiCall {
            remoteDataSource.getExpenses(
                month = month,
                year = year,
                cursor = cursor,
                pageSize = pageSize,
                currencyCode = currencyCode
            )
        }
        response.body?.let { list ->
            val storedAt = Date()
            val itemsEntities: List<ExpenseEntity> =
                list.expensesList.expenses.map {
                    it.expenseFragment.toEntity(
                        userUid = userUid,
                        storedAt = storedAt
                    )
                }
            localDataSource.saveExpenseList(itemsEntities)
        }
        return response.body?.expensesList?.expenses?.size ?: 0
    }

    override suspend fun saveExpenses(expense: ExpenseEntity) {
        localDataSource.saveExpenseList(listOf(expense))
    }

    /*override fun getExpenses(
        month: Int,
        year: Int,
        pageNumber: Int,
        pageSize: Int?,
        userUid: String,
        previousResponse: ExpenseList?
    ): Flow<ResponseWrapper<ExpenseList>> = flow {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DATE, -1)
        emit(ResponseWrapper.loading(body = previousResponse))
        val items = localDataSource.getExpenses(
            month = month,
            year = year,
            page = pageNumber,
            limit = pageSize ?: 20,
            userUuid = userUid,
            expirationDate = calendar.time
        )
        if (items.isEmpty()) {
            val remoteItems = remoteDataSource.getExpenses(
                month = month,
                year = year,
                pageNumber = pageNumber,
                pageSize = pageSize
            )
            val storedAt = Date()
            remoteItems.data?.expensesList?.let { list ->
                val itemsEntities: List<ExpenseEntity> =
                    list.expenses.map {
                        it.toEntity(
                            userUid = userUid,
                            storedAt = storedAt
                        )
                    }
                localDataSource.saveExpenseList(itemsEntities)
                emit(
                    ResponseWrapper.success(
                        ExpenseList(
                            totalItems = list.pagination.paginationFragment.totalItems,
                            totalPages = list.pagination.paginationFragment.totalPages,
                            items = itemsEntities
                        )
                    )
                )
            }
        } else {
            emit(ResponseWrapper.success(ExpenseList(items = items)))
        }
    }*/

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
