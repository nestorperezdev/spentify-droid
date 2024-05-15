package com.nestor.database.data.expense

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import java.util.Date

@Dao
interface ExpenseDao {
    @Upsert
    suspend fun upsertAll(expenses: List<ExpenseEntity>)

    @Query("SELECT * FROM expense WHERE date BETWEEN :from AND :to AND user_uuid = :userUuid ORDER BY `order` DESC LIMIT :limit OFFSET :page * :limit")
    suspend fun getExpenses(
        from: Date,
        to: Date,
        userUuid: String,
        page: Int,
        limit: Int
    ): List<ExpenseEntity>
}