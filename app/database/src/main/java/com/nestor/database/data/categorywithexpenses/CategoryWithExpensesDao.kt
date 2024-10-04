package com.nestor.database.data.categorywithexpenses

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface CategoryWithExpensesDao {
    @Transaction
    @Query("SELECT * FROM category")
    suspend fun getCategoryWithExpenses(): List<CategoryWithExpenses>
}