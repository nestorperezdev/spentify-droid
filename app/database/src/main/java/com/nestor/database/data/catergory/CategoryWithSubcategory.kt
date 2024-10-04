package com.nestor.database.data.catergory

import androidx.room.Embedded
import androidx.room.Relation
import com.nestor.database.data.subcategory.SubCategoryEntity

data class CategoryWithSubcategory(
    @Embedded val categoryEntity: CategoryEntity,
    @Relation(
        parentColumn = "subcategoryId",
        entityColumn = "id"
    ) val subcategoryEntity: SubCategoryEntity
)
