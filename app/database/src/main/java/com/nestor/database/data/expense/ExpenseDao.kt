package com.nestor.database.data.expense

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.nestor.database.data.expensewithcategory.ExpenseWithCategoryEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface ExpenseDao {
    @Upsert
    suspend fun upsertAll(expenses: List<ExpenseEntity>)

    @Deprecated("Use expenseWithCategory.getExpensesWithCategory instead")
    @Query("SELECT * FROM expense WHERE date BETWEEN :from AND :to AND user_uuid = :userUuid AND stored_at > :expirationDate AND currency_code = :currencyCode ORDER BY cursor DESC")
    fun getExpenses(
        from: Date,
        to: Date,
        userUuid: String,
        expirationDate: Date,
        currencyCode: String
    ): Flow<List<ExpenseEntity>>

    @Transaction
    @Query("SELECT * FROM expense WHERE date BETWEEN :from AND :to")
    fun getExpenseWithCategory(
        from: Date,
        to: Date
    ): Flow<List<ExpenseWithCategoryEntity>>

    @Transaction
    @Query("SELECT * FROM expense WHERE date BETWEEN :from AND :to")
    fun getExpenseWithCategoryAndSubcategory(
        from: Date,
        to: Date
    ): Flow<List<ExpenseWithCategoryAndSubcategory>>

    @Delete
    suspend fun delete(expense: ExpenseEntity)
}