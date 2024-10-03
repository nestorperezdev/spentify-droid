package com.nestor.expenses.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.nestor.schema.utils.ResponseWrapper
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.input.FormFieldData
import kotlinx.coroutines.flow.MutableStateFlow

@Preview
@Composable
fun NewExpenseScreenContentPreview() {
    SpentifyTheme {
        NewExpenseScreenContent(
            amountField = FormFieldData(""),
            onAmountChanged = {},
            isLoading = MutableStateFlow(false),
            descriptionText = FormFieldData(""),
            onDescriptionChanged = {},
            selectedCurrencyEntity = MutableStateFlow(null),
            categories = MutableStateFlow(ResponseWrapper.loading()),
            onCategorySelected = {},
            categorySelected = MutableStateFlow(null)
        )
    }
}