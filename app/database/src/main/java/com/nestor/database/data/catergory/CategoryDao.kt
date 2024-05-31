package com.nestor.database.data.catergory

import androidx.room.Dao
import androidx.room.Upsert

@Dao
interface CategoryDao {
    @Upsert
    fun insertCategories(category: List<CategoryEntity>)
}
