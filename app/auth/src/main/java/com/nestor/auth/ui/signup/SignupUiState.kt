package com.nestor.auth.ui.signup

import com.nestor.uikit.input.FormFieldData

data class SignupUiState(
    val email: FormFieldData = FormFieldData(""),
    val name: FormFieldData = FormFieldData(""),
    val password: FormFieldData = FormFieldData(""),
    val repeatPassword: FormFieldData = FormFieldData(""),
    val isLoading: Boolean = false,
    val signupErrorResource: Int? = null,
    val isRegistered: Boolean = false
)
