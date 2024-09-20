package com.nestor.account.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nestor.common.ui.currencypicker.CurrencyPickerBottomSheet
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.list.SYList
import com.nestor.uikit.list.SYListItemData
import com.nestor.uikit.theme.color.Blue50
import com.nestor.uikit.theme.spacing.LocalSYPadding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.nestor.uikit.R as UIKitR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    vm: AccountViewModel = hiltViewModel()
) {
    AccountScreenContent(
        modifier = modifier,
        username = "Nestor",
        onLogoutClick = vm::logout,
        onCurrencyClick = vm::onCurrencyClick,
        appVersion = vm.appVersion
    )
    val currencyPickerVisible by vm.currencyPickerVisible.collectAsState()
    val currencyPickerState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    LaunchedEffect(currencyPickerVisible) {
        if (currencyPickerVisible) {
            currencyPickerState.show()
        }
    }
    if (currencyPickerVisible) {
        val selectedCurrency by vm.selectedCurrency.collectAsState()
        CurrencyPickerBottomSheet(
            bottomSheetState = currencyPickerState,
            onDismissRequest = { vm.onCurrencyPickerDismiss() },
            initialValue = selectedCurrency,
            onCurrencySelected = vm::onCurrencySelected
        )
    }
}

@Composable
private fun AccountScreenContent(
    modifier: Modifier = Modifier,
    username: String,
    appVersion: StateFlow<String>,
    onLogoutClick: () -> Unit = {},
    onCurrencyClick: () -> Unit = {}
) {
    var privacyDialogIsVisible by remember { mutableStateOf(false) }
    var tocsDialogIsVisible by remember { mutableStateOf(false) }
    if (privacyDialogIsVisible) {
        PrivacyDialog {
            privacyDialogIsVisible = false
        }
    }
    if (tocsDialogIsVisible) {
        TOCSDialog {
            tocsDialogIsVisible = false
        }
    }
    Column(
        modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        AccountIcon(username = "Nestor", modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "$username's Profile", style = MaterialTheme.typography.titleSmall)
        Spacer(modifier = Modifier.height(40.dp))
        SYList(
            onItemClick = {
                if (it.key == "currency") {
                    onCurrencyClick()
                }
            },
            items = listOf(
                SYListItemData(
                    key = "currency",
                    label = "Change currency",
                    leadingIcon = SYListItemData.SYListItemIcon(
                        icon = painterResource(id = UIKitR.drawable.baseline_attach_money_24),
                        tint = MaterialTheme.colorScheme.primary
                    ),
                    trailingIcon = SYListItemData.SYListItemIcon(
                        icon = painterResource(id = UIKitR.drawable.baseline_arrow_forward_ios_24),
                        tint = MaterialTheme.colorScheme.onSurface,
                        foregroundTint = null
                    )
                ),
                SYListItemData(
                    label = "Edit name",
                    trailingIcon = SYListItemData.SYListItemIcon(
                        icon = painterResource(id = UIKitR.drawable.baseline_arrow_forward_ios_24),
                        tint = MaterialTheme.colorScheme.onSurface,
                        foregroundTint = null
                    )
                ),
                SYListItemData(
                    label = "Change password",
                    leadingIcon = SYListItemData.SYListItemIcon(
                        icon = painterResource(id = UIKitR.drawable.baseline_lock_open_24),
                        tint = MaterialTheme.colorScheme.primary
                    ),
                ),
            )
        )
        Spacer(modifier = Modifier.height(48.dp))
        Text(text = "About Spentify", style = MaterialTheme.typography.titleSmall)
        Spacer(modifier = Modifier.height(40.dp))
        SYList(
            onItemClick = {
                if (it.key == "privacy") {
                    privacyDialogIsVisible = true
                } else if (it.key == "tocs") {
                    tocsDialogIsVisible = true
                }
            },
            items = listOf(
                SYListItemData(
                    label = "Privacy",
                    key = "privacy",
                    leadingIcon = SYListItemData.SYListItemIcon(
                        icon = painterResource(id = UIKitR.drawable.baseline_help_24),
                        tint = MaterialTheme.colorScheme.primary
                    ),
                    trailingIcon = SYListItemData.SYListItemIcon(
                        icon = painterResource(id = UIKitR.drawable.baseline_arrow_forward_ios_24),
                        tint = MaterialTheme.colorScheme.onSurface,
                        foregroundTint = null
                    )
                ),
                SYListItemData(
                    label = "Terms & Conditions",
                    key = "tocs",
                    leadingIcon = SYListItemData.SYListItemIcon(
                        icon = painterResource(id = UIKitR.drawable.baseline_help_24),
                        tint = MaterialTheme.colorScheme.primary
                    ),
                    trailingIcon = SYListItemData.SYListItemIcon(
                        icon = painterResource(id = UIKitR.drawable.baseline_arrow_forward_ios_24),
                        tint = MaterialTheme.colorScheme.onSurface,
                        foregroundTint = null
                    )
                )
            )
        )
        Spacer(modifier = Modifier.height(48.dp))
        SYList(
            onItemClick = {
                if (it.key == "logout") {
                    onLogoutClick()
                }
            },
            items = listOf(
                SYListItemData(
                    label = "Logout",
                    trailingIcon = SYListItemData.SYListItemIcon(
                        icon = painterResource(id = UIKitR.drawable.baseline_exit_to_app_24),
                        tint = MaterialTheme.colorScheme.error
                    ),
                    key = "logout",
                ),
                SYListItemData(
                    label = "Delete Account",
                    leadingIcon = SYListItemData.SYListItemIcon(
                        icon = painterResource(id = UIKitR.drawable.baseline_warning_24),
                        tint = MaterialTheme.colorScheme.error,
                    ),
                    trailingIcon = SYListItemData.SYListItemIcon(
                        icon = painterResource(id = UIKitR.drawable.baseline_delete_24),
                        tint = MaterialTheme.colorScheme.error,
                    )
                ),
            )
        )
        Text(
            text = appVersion.collectAsState().value,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun AccountIcon(
    modifier: Modifier = Modifier,
    username: String
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(Blue50)
            .size(72.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "NP",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )
    }
}


@Preview
@Composable
fun AccountScreenPreview() {
    SpentifyTheme {
        Scaffold {
            AccountScreenContent(
                modifier = Modifier
                    .padding(it)
                    .padding(LocalSYPadding.current.screenHorizontalPadding),
                username = "Nestor",
                appVersion = MutableStateFlow("1.0.0.500.preview")
            )
        }
    }
}