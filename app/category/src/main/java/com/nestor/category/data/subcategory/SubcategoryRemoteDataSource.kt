package com.nestor.category.data.subcategory

import com.nestor.schema.CategoryListQuery
import com.nestor.schema.utils.ResponseWrapper

interface SubcategoryRemoteDataSource {
    suspend fun getCategories(): ResponseWrapper<CategoryListQuery.Data>
}