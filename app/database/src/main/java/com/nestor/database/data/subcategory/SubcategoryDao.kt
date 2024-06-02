package com.nestor.database.data.subcategory

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SubcategoryDao {
    @Transaction
    @Query("SELECT * FROM subcategory")
    fun getSubcategoriesWithCategories(): Flow<List<SubcategoryWithCategories>>

    @Upsert
    suspend fun insertSubcategory(subcategories: List<SubCategoryEntity>)
}
