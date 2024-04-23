package com.nestor.expenses.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nestor.expenses.R
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.button.SYButton
import com.nestor.uikit.keyboard.SYKeyboard
import com.nestor.uikit.statusbar.NavigationIcon
import com.nestor.uikit.statusbar.SYStatusBar
import com.nestor.uikit.statusbar.StatusBarType
import com.nestor.uikit.theme.spacing.LocalSYPadding

@Composable
fun NewExpenseScreen(
    viewModel: NewExpenseViewModel = hiltViewModel(),
    onNavBack: () -> Unit = {},
) {
    NewExpenseScreenContent(
        onNavBack = onNavBack,
        onKeyPressed = viewModel::onKeyPressed,
        onSaveClick = viewModel::onSave
    )
}

@Composable
private fun NewExpenseScreenContent(
    onNavBack: () -> Unit = {},
    onKeyPressed: (Key) -> Unit = {},
    onSaveClick: () -> Unit = {}
) {
    Scaffold(topBar = { NewExpenseToolbar(onNavBack) }) {
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
            Text(text = "USD 100.0 $", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.weight(1f))
            SYKeyboard(modifier = Modifier.fillMaxWidth(), onKeyPress = onKeyPressed)
            SYButton(
                onClick = onSaveClick,
                buttonText = "Save",
                modifier = Modifier.fillMaxWidth()
            )
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
        NewExpenseScreenContent(onKeyPressed = {})
    }
}