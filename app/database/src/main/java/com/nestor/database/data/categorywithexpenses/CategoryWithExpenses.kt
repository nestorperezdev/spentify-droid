package com.nestor.database.data.categorywithexpenses

import androidx.room.Embedded
import androidx.room.Relation
import com.nestor.database.data.catergory.CategoryEntity
import com.nestor.database.data.expense.ExpenseEntity

data class CategoryWithExpenses(
    @Embedded val category: CategoryEntity,
    @Relation(parentColumn = "id", entityColumn = "categoryId")
    val expenses: List<ExpenseEntity>
)
