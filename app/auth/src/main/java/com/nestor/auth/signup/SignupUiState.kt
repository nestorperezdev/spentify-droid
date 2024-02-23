package com.nestor.auth.signup

import com.nestor.uikit.input.FormFieldData

data class SignupUiState(
    val email: FormFieldData = FormFieldData(""),
    val name: FormFieldData = FormFieldData(""),
    val password: FormFieldData = FormFieldData(""),
    val repeatPassword: FormFieldData = FormFieldData(""),
)
