package com.nestor.database.data.expense

import androidx.room.Embedded
import androidx.room.Relation
import com.nestor.database.data.catergory.CategoryEntity
import com.nestor.database.data.catergory.CategoryWithSubcategory

data class ExpenseWithCategoryAndSubcategory(
    @Embedded val expense: ExpenseEntity,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id",
        entity = CategoryEntity::class
    ) val category: CategoryWithSubcategory
)
