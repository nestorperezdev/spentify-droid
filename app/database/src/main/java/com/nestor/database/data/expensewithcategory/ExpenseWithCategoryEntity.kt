package com.nestor.database.data.expensewithcategory

import androidx.room.Embedded
import androidx.room.Relation
import com.nestor.database.data.catergory.CategoryEntity
import com.nestor.database.data.expense.ExpenseEntity

data class ExpenseWithCategoryEntity(
    @Embedded val expense: ExpenseEntity,
    @Relation(parentColumn = "categoryId", entityColumn = "id") val category: CategoryEntity?,
)
