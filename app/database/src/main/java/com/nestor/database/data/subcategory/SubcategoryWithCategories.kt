package com.nestor.database.data.subcategory

import androidx.room.Embedded
import androidx.room.Relation
import com.nestor.database.data.catergory.CategoryEntity

data class SubcategoryWithCategories(
    @Embedded val subCategory: SubCategoryEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "subcategoryId"
    ) val categories: List<CategoryEntity>
)
