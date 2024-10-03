package com.nestor.expenses.ui.category

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nestor.database.data.catergory.CategoryEntity
import com.nestor.database.data.subcategory.SubCategoryEntity
import com.nestor.database.data.subcategory.SubcategoryWithCategories
import com.nestor.schema.utils.ResponseWrapper
import com.nestor.uikit.SpentifyTheme

@Preview
@Composable
fun CategoryPickerSheetPreview() {
    SpentifyTheme {
        Scaffold {
            CategoryPickerDialogContent(
                modifier = Modifier.padding(it),
                categories = ResponseWrapper.success(
                    List(2) {
                        SubcategoryWithCategories(
                            subCategory = SubCategoryEntity(
                                id = "id",
                                name = "Subcategory",
                                icon = "icon"
                            ),
                            categories = listOf(
                                CategoryEntity(
                                    id = "id",
                                    name = "Category",
                                    icon = "icon",
                                    tint = null,
                                    subcategoryId = "id"
                                ),
                                CategoryEntity(
                                    id = "id",
                                    name = "Category",
                                    icon = "icon",
                                    tint = null,
                                    subcategoryId = "id"
                                ),
                            )
                        )
                    }
                ),
                onCategorySelected = {}
            )
        }
    }
}