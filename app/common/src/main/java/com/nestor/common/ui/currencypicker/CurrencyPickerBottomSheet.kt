@file:OptIn(ExperimentalMaterial3Api::class)

package com.nestor.common.ui.currencypicker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nestor.database.data.currency.CurrencyEntity
import com.nestor.uikit.input.FormFieldData
import com.nestor.uikit.list.SYListItem
import com.nestor.uikit.list.SYListItemData
import com.nestor.uikit.theme.spacing.LocalSYPadding
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.nestor.uikit.R as UIR

private const val TAG = "CurrencyPickerBottomShe"

@Composable
fun CurrencyPickerBottomSheet(
    bottomSheetState: SheetState = rememberModalBottomSheetState(),
    viewModel: CurrencyPickerViewModel = hiltViewModel(),
    initialValue: CurrencyEntity? = null,
    onDismissRequest: () -> Unit = {},
    onCurrencySelected: (String) -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(initialValue) {
        viewModel.setInitialCurrencyValue(initialValue)
    }
    CurrencyPickerBottomSheetContent(
        bottomSheetState = bottomSheetState,
        filterTextState = viewModel.filterText,
        onFilterTextChange = viewModel::onFilterTextChange,
        onDismissRequest = onDismissRequest,
        currenciesListState = viewModel.filteredCurrencies,
        selectedCurrencyState = viewModel.selectedCurrency,
        onCurrencySelected = {
            onCurrencySelected(it)
            viewModel.onCurrencySelected(it)
            coroutineScope.launch(NonCancellable) {
                try {
                    //  TODO: investigate why hide is throwing scopeCoroutine was cancelled
                    bottomSheetState.hide()
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    onDismissRequest()
                }
            }
        }
    )
}

@Composable
private fun CurrencyPickerBottomSheetContent(
    modifier: Modifier = Modifier,
    bottomSheetState: SheetState,
    filterTextState: StateFlow<FormFieldData>,
    onFilterTextChange: (String) -> Unit,
    onDismissRequest: () -> Unit = {},
    currenciesListState: StateFlow<List<CurrencyEntity>>,
    selectedCurrencyState: StateFlow<CurrencyEntity?> = MutableStateFlow(null),
    onCurrencySelected: (String) -> Unit = {}
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = bottomSheetState,
        modifier = modifier.statusBarsPadding(),
    ) {
        Column(modifier = Modifier.padding(horizontal = LocalSYPadding.current.screenHorizontalPadding)) {
            val filterText = filterTextState.collectAsState()
            val coroutineScope = rememberCoroutineScope()
            OutlinedTextField(
                value = filterText.value.value,
                onValueChange = onFilterTextChange,
                label = { Text("Search a currency") },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusEvent {
                        if (it.isFocused) {
                            coroutineScope.launch { bottomSheetState.expand() }
                        }
                    }
            )
            val selectedCurrency = selectedCurrencyState.collectAsState().value
            val iconResource = painterResource(id = UIR.drawable.baseline_check_24)
            val primaryColor = MaterialTheme.colorScheme.primary
            val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
            val icon = remember {
                SYListItemData.SYListItemIcon(
                    icon = iconResource,
                    foregroundTint = onPrimaryColor,
                    tint = primaryColor
                )
            }
            val currenciesList = currenciesListState.collectAsState().value
            val currenciesListItems = remember(currenciesList, selectedCurrency) {
                currenciesList.map {
                    val isSelected = selectedCurrency?.code == it.code
                    SYListItemData(
                        label = it.name,
                        key = it.code,
                        subtitle = "${it.code} - ${it.symbol}",
                        trailingIcon = if (isSelected) icon else null
                    )
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item { Spacer(modifier = Modifier.height(32.dp)) }
                items(currenciesListItems) { currency ->
                    SYListItem(
                        item = currency,
                        modifier = Modifier.clickable { onCurrencySelected(currency.key) },
                    )
                }
                item { Spacer(modifier = Modifier.height(32.dp)) }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
