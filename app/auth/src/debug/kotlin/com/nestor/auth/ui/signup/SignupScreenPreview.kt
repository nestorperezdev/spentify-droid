package com.nestor.auth.ui.signup

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
fun SignupScreenContentPreview() {
    SpentifyTheme {
        var name by remember { mutableStateOf(FormFieldData("")) }
        var email by remember { mutableStateOf(FormFieldData("")) }
        var password by remember { mutableStateOf(FormFieldData("")) }
        var repeatPassword by remember { mutableStateOf(FormFieldData("")) }
        SignupScreenContent(
            onLoginClick = {},
            onNavigationBackClick = {},
            name = name,
            onNameChange = { name = name.copy(value = it) },
            email = email,
            onEmailChange = { email = email.copy(value = it) },
            password = password,
            onPasswordChanged = { password = password.copy(value = it) },
            repeatPassword = repeatPassword,
            onRecoverPasswordClick = {},
            alreadyRegistered = true,
            showFormInvalidToast = false,
            onShowFormInvalidToastDismissed = {},
            onRepeatPasswordChanged = { repeatPassword = repeatPassword.copy(value = it) }
        )
    }
}