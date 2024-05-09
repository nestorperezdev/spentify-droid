package com.nestor.expenses.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nestor.expenses.R
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.button.SYButton
import com.nestor.uikit.input.FormFieldData
import com.nestor.uikit.loading.LoadingScreen
import com.nestor.uikit.statusbar.NavigationIcon
import com.nestor.uikit.statusbar.SYStatusBar
import com.nestor.uikit.statusbar.StatusBarType
import com.nestor.uikit.theme.spacing.LocalSYPadding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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
    NewExpenseScreenContent(
        onNavBack = onNavBack,
        onSaveClick = viewModel::onSave,
        onAmountChanged = viewModel::onAmountChanged,
        amountField = viewModel.amount.collectAsState().value,
        isLoading = viewModel.loadingState
    )
}

@Composable
private fun NewExpenseScreenContent(
    onNavBack: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    amountField: FormFieldData,
    onAmountChanged: (String) -> Unit,
    isLoading: StateFlow<Boolean>
) {
    Scaffold(topBar = { NewExpenseToolbar(onNavBack) }) {
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
                    TextField(
                        value = amountField.value,
                        onValueChange = onAmountChanged,
                        prefix = {
                            Text(
                                text = "USD ",
                                style = MaterialTheme.typography.titleLarge
                            )
                        },
                        suffix = { Text(text = "$", style = MaterialTheme.typography.titleLarge) },
                        textStyle = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        minLines = 1,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Decimal
                        ),
                        keyboardActions = KeyboardActions(onDone = { onSaveClick() })
                    )
                }
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
            isLoading = MutableStateFlow(false)
        )
    }
}