package com.nestor.expenses.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nestor.common.ui.currencypicker.CurrencyPickerBottomSheet
import com.nestor.database.data.currency.CurrencyEntity
import com.nestor.database.data.subcategory.SubcategoryWithCategories
import com.nestor.expenses.R
import com.nestor.schema.utils.ResponseWrapper
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.button.SYButton
import com.nestor.uikit.input.FormFieldData
import com.nestor.uikit.list.SYListItem
import com.nestor.uikit.list.SYListItemData
import com.nestor.uikit.loading.LoadingScreen
import com.nestor.uikit.statusbar.NavigationIcon
import com.nestor.uikit.statusbar.SYStatusBar
import com.nestor.uikit.statusbar.StatusBarType
import com.nestor.uikit.theme.spacing.LocalSYPadding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewExpenseScreen(
    viewModel: NewExpenseViewModel = hiltViewModel(),
    onNavBack: () -> Unit = {},
) {
    val dismissDialog = viewModel.dismissDialog.collectAsState().value
    LaunchedEffect(dismissDialog) {
        if (dismissDialog) {
            onNavBack()
        }
    }
    val currencyPickerOpen = viewModel.currencyOpenState.collectAsState().value
    val currencyPickerState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    NewExpenseScreenContent(
        onNavBack = onNavBack,
        onSaveClick = viewModel::onSave,
        onAmountChanged = viewModel::onAmountChanged,
        amountField = viewModel.amount.collectAsState().value,
        isLoading = viewModel.loadingState,
        descriptionText = viewModel.description.collectAsState().value,
        onDescriptionChanged = viewModel::onDescriptionChanged,
        selectedCurrencyEntity = viewModel.selectedCurrency,
        onCurrencyClicked = viewModel::onCurrencyPickerClicked,
        categories = viewModel.categories
    )
    val coroutineScope = rememberCoroutineScope()
    //  TODO: Fix animation
    if (currencyPickerOpen) {
        LaunchedEffect(currencyPickerOpen) {
            coroutineScope.launch {
                currencyPickerState.show()
            }
        }
        val currencySelected = viewModel.selectedCurrency.collectAsState()
        CurrencyPickerBottomSheet(
            bottomSheetState = currencyPickerState,
            onCurrencySelected = viewModel::onCurrencySelected,
            onDismissRequest = viewModel::onCurrencyPickerDismiss,
            initialValue = currencySelected.value
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewExpenseScreenContent(
    onNavBack: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    amountField: FormFieldData,
    onAmountChanged: (String) -> Unit,
    descriptionText: FormFieldData,
    onDescriptionChanged: (String) -> Unit,
    selectedCurrencyEntity: StateFlow<CurrencyEntity?>,
    isLoading: StateFlow<Boolean>,
    categories: StateFlow<ResponseWrapper<List<SubcategoryWithCategories>>>,
    onCurrencyClicked: () -> Unit = {}
) {
    var isCategoryPickerExpended by remember { mutableStateOf(false) }
    Scaffold(topBar = { NewExpenseToolbar(onNavBack) }) {
        if (isCategoryPickerExpended) {
            CategoryPickerSheet(categories = categories) {
                isCategoryPickerExpended = false
            }
        }
        if (isLoading.collectAsState().value.not()) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .padding(LocalSYPadding.current.screenHorizontalPadding)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(50.dp))
                Text(
                    text = stringResource(R.string.enter_the_amount_you_want_to_register),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(40.dp))
                CompositionLocalProvider(LocalTextInputService provides LocalTextInputService.current) {
                    val selectedCurrency = selectedCurrencyEntity.collectAsState().value
                    OutlinedTextField(
                        value = amountField.value,
                        onValueChange = onAmountChanged,
                        prefix = {
                            Text(
                                text = "${selectedCurrency?.code ?: ""} ",
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.clickable { onCurrencyClicked() }
                            )
                        },
                        suffix = {
                            Text(
                                text = selectedCurrency?.symbol ?: "",
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.clickable { onCurrencyClicked() }
                            )
                        },
                        textStyle = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        minLines = 1,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Decimal
                        ),
                        keyboardActions = KeyboardActions(onDone = { onSaveClick() }),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(40.dp))
                OutlinedTextField(
                    value = descriptionText.value,
                    onValueChange = onDescriptionChanged,
                    label = { Text(stringResource(R.string.description)) },
                    supportingText = { Text("${descriptionText.value.length} / 255") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(40.dp))
                SYListItem(
                    item = SYListItemData(label = "Pick a category"),
                    modifier = Modifier.clickable { isCategoryPickerExpended = true }
                )
                Spacer(modifier = Modifier.weight(1f))
                SYButton(
                    onClick = onSaveClick,
                    buttonText = "Save",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            LoadingScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryPickerSheet(
    sheetState: SheetState = rememberModalBottomSheetState(),
    categories: StateFlow<ResponseWrapper<List<SubcategoryWithCategories>>>,
    onDismissRequest: () -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismissRequest, sheetState = sheetState) {
        CategoryPickerDialogContent(
            modifier = Modifier,
            categories = categories.collectAsState().value
        )
    }
}

@Composable
private fun CategoryPickerDialogContent(modifier: Modifier = Modifier,
                                        categories: ResponseWrapper<List<SubcategoryWithCategories>>
) {
    Column(modifier = modifier) {
        if (categories.isLoading) {
            Text(text = "Loading")
        }
        categories.body?.let {
            it.forEach { subcat ->
                Text(text = subcat.subCategory.name)
            }
        }
    }
}

@Composable
private fun NewExpenseToolbar(onNavBack: () -> Unit) {
    SYStatusBar(
        barType = StatusBarType.NavigationWithTitle(
            title = "Register new Expense",
            navigation = NavigationIcon.Close { onNavBack() }
        )
    )
}

@Preview
@Composable
private fun NewExpenseScreenContentPreview() {
    SpentifyTheme {
        NewExpenseScreenContent(
            amountField = FormFieldData(""),
            onAmountChanged = {},
            isLoading = MutableStateFlow(false),
            descriptionText = FormFieldData(""),
            onDescriptionChanged = {},
            selectedCurrencyEntity = MutableStateFlow(null),
            categories = MutableStateFlow(ResponseWrapper.loading())
        )
    }
}

@Preview
@Composable
private fun CategoryPickerSheetPreview() {
    SpentifyTheme {
        Scaffold {
            CategoryPickerDialogContent(
                modifier = Modifier.padding(it),
                categories = ResponseWrapper.loading()
            )
        }
    }
}