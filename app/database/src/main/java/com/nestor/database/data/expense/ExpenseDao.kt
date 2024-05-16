package com.nestor.database.data.expense

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface ExpenseDao {
    @Upsert
    suspend fun upsertAll(expenses: List<ExpenseEntity>)

    @Query("SELECT * FROM expense WHERE date BETWEEN :from AND :to AND user_uuid = :userUuid AND stored_at > :expirationDate ORDER BY `order` DESC")
    fun getExpenses(
        from: Date,
        to: Date,
        userUuid: String,
        expirationDate: Date
    ): Flow<List<ExpenseEntity>>
}