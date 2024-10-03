package com.nestor.auth.ui.recoverpassword

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.input.FormFieldData

@Preview
@Composable
fun RecoverPasswordScreenContentPreview() {
    SpentifyTheme {
        var password by remember { mutableStateOf(FormFieldData("")) }
        var repeatPassword by remember { mutableStateOf(FormFieldData("")) }
        RecoverPasswordScreenContent(
            onNavigationBackClick = {},
            password = password,
            onPasswordChanged = { password = password.copy(value = it) },
            repeatPassword = repeatPassword,
            showFormInvalidToast = false,
            onShowFormInvalidToastDismissed = {},
            onRepeatPasswordChanged = { repeatPassword = repeatPassword.copy(value = it) }
        )
    }
}

@Preview
@Composable
fun RecoverPasswordContentPreview() {
    SpentifyTheme {
        RecoverPasswordSuccessScreen(onGetStartedClick = {})
    }
}