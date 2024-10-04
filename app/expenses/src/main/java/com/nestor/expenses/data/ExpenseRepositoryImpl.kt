package com.nestor.expenses.data

import android.util.Log
import com.nestor.database.data.expense.ExpenseEntity
import com.nestor.database.data.expensewithcategory.ExpenseWithCategoryEntity
import com.nestor.expenses.data.expensewithcategory.ExpenseWithCategoryLocalDataSource
import com.nestor.schema.fragment.ExpenseFragment
import com.nestor.schema.type.ExpenseInput
import com.nestor.schema.utils.safeApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
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
        currencyCode = selectedCurrency.currencyInfo.code,
        categoryId = category?.category?.id
    )
}

private const val TAG = "ExpenseRepositoryImpl"

class ExpenseRepositoryImpl @Inject constructor(
    private val remoteDataSource: ExpenseRemoteDataSource,
    private val localDataSource: ExpenseLocalDataSource,
    private val expenseWithCategoryLocalDataSource: ExpenseWithCategoryLocalDataSource
) : ExpenseRepository {

    override suspend fun createExpense(expenseInput: ExpenseInput) =
        safeApiCall { remoteDataSource.createExpense(expenseInput) }

    override fun getExpenses(
        month: Int,
        year: Int,
        userUid: String,
        currencyCode: String
    ): Flow<List<ExpenseWithCategoryEntity>> {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DATE, -1)
        return expenseWithCategoryLocalDataSource.getExpensesWithCategory(
            month = month,
            year = year,
            userUuid = userUid,
            expirationDate = calendar.time,
            currencyCode = currencyCode
        ).onEach { expenses ->
            Log.i(TAG, "getExpenses: $expenses")
        }
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

    override suspend fun deleteExpense(expense: ExpenseEntity) {
        localDataSource.deleteExpense(expense)
        remoteDataSource.deleteExpense(expense.id)
    }
}
