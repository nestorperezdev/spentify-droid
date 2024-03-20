package com.nestor.auth.ui.login

import com.nestor.uikit.input.FormFieldData

data class LoginUiState(
    val email: FormFieldData = FormFieldData(""),
    val password: FormFieldData = FormFieldData(""),
    val isLoading: Boolean = false,
    val loginErrorResource: Int? = null,
    val isSuccess: Boolean = false
)
