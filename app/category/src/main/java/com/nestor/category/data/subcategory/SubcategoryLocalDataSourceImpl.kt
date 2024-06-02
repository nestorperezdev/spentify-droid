package com.nestor.category.data.subcategory

import com.nestor.database.data.catergory.CategoryDao
import com.nestor.database.data.subcategory.SubCategoryEntity
import com.nestor.database.data.subcategory.SubcategoryDao
import com.nestor.database.data.subcategory.SubcategoryWithCategories
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubcategoryLocalDataSourceImpl @Inject constructor(
    private val subcategoryDao: SubcategoryDao
) : SubcategoryLocalDataSource {
    override fun getSubcategories(): Flow<List<SubcategoryWithCategories>> {
        return subcategoryDao.getSubcategoriesWithCategories()
    }

    override suspend fun saveSubcategoriesWithCategories(data: List<SubCategoryEntity>) {
        subcategoryDao.insertSubcategory(data)
    }
}