package com.nestor.category.data.subcategory

import com.nestor.database.data.subcategory.SubCategoryEntity
import com.nestor.database.data.subcategory.SubcategoryWithCategories
import com.nestor.schema.utils.ResponseWrapper
import kotlinx.coroutines.flow.Flow

interface SubcategoryRepository {
    fun getSubcategories(): Flow<ResponseWrapper<List<SubcategoryWithCategories>>>
    suspend fun saveSubcategories(categories: List<SubcategoryWithCategories>)
}