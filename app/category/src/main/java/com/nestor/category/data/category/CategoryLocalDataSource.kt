package com.nestor.category.data.category

import com.nestor.database.data.catergory.CategoryEntity

interface CategoryLocalDataSource {
    suspend fun saveCategories(categories: List<CategoryEntity>)
}