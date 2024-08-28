package com.nestor.category.data.subcategory

import com.nestor.category.data.category.CategoryLocalDataSource
import com.nestor.common.util.onEachEmpty
import com.nestor.database.data.catergory.CategoryEntity
import com.nestor.database.data.subcategory.SubCategoryEntity
import com.nestor.database.data.subcategory.SubcategoryWithCategories
import com.nestor.schema.CategoryListQuery
import com.nestor.schema.utils.ResponseWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SubcategoryRepositoryImpl @Inject constructor(
    private val subcategoryLocalDataSource: SubcategoryLocalDataSource,
    private val subcategoryRemoteDataSource: SubcategoryRemoteDataSource,
    private val categoryLocalDataSource: CategoryLocalDataSource
) : SubcategoryRepository {
    override fun getSubcategories(): Flow<ResponseWrapper<List<SubcategoryWithCategories>>> = flow {
        val result = subcategoryLocalDataSource
            .getSubcategories()
            .onEachEmpty { syncSubcategories() }
            .filterNot { it.isEmpty() }
            .map { ResponseWrapper.success(it) }
        emitAll(result)
    }

    private suspend fun syncSubcategories() {
        val categories = subcategoryRemoteDataSource.getCategories()
        categories.body?.let { saveSubcategories(it.toCategoryEntity()) }
    }

    override suspend fun saveSubcategories(categories: List<SubcategoryWithCategories>) {
        subcategoryLocalDataSource.saveSubcategoriesWithCategories(categories.map { it.subCategory })
        categories
            .map { it.categories }
            .reduce { acc, subcategoryWithCategories -> subcategoryWithCategories + acc }
            .let { categoryLocalDataSource.saveCategories(it) }
    }
}

private fun CategoryListQuery.Data.toCategoryEntity(): List<SubcategoryWithCategories> {
    return this.categoryList.categoryListFragment.groups.map { it.subcategory }.map { subcategory ->
        SubcategoryWithCategories(
            subCategory = SubCategoryEntity(
                icon = subcategory.iconId,
                name = subcategory.subcategoryName,
                id = subcategory.id
            ),
            categories = subcategory.categories.map { it.category }.map { category ->
                CategoryEntity(
                    id = category.id,
                    name = category.categoryName,
                    icon = category.iconId,
                    subcategoryId = subcategory.id,
                    tint = category.tint?.toInt()
                )
            }
        )
    }
}