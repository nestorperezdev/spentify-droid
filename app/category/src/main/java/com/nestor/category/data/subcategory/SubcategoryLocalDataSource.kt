package com.nestor.category.data.subcategory

import com.nestor.database.data.subcategory.SubCategoryEntity
import com.nestor.database.data.subcategory.SubcategoryWithCategories
import kotlinx.coroutines.flow.Flow

interface SubcategoryLocalDataSource {
    fun getSubcategories(): Flow<List<SubcategoryWithCategories>>
    suspend fun saveSubcategoriesWithCategories(data: List<SubCategoryEntity>)
}