package com.nestor.category.data.category

import com.nestor.database.data.catergory.CategoryDao
import com.nestor.database.data.catergory.CategoryEntity
import javax.inject.Inject

class CategoryLocalDataSourceImpl @Inject constructor(private val dao: CategoryDao) : CategoryLocalDataSource {
    override suspend fun saveCategories(categories: List<CategoryEntity>) {
        dao.insertCategories(categories)
    }
}