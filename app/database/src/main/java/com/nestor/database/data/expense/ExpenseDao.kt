package com.nestor.database.data.expense

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import java.util.Date

@Dao
interface ExpenseDao {
    @Upsert
    suspend fun upsertAll(expenses: List<ExpenseEntity>)

    @Query("SELECT * FROM expenseentity WHERE date BETWEEN :from AND :to AND id > :afterCursor AND user_uuid = :userUuid LIMIT :limit")
    suspend fun getExpenses(
        from: Date,
        to: Date,
        afterCursor: Int,
        userUuid: String,
        limit: Int
    ): List<ExpenseEntity>
}