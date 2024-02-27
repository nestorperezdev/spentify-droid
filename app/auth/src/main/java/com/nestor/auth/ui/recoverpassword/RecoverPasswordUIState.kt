package com.nestor.auth.ui.recoverpassword

import com.nestor.uikit.input.FormFieldData

data class RecoverPasswordUIState(
    val password: FormFieldData = FormFieldData(""),
    val repeatPassword: FormFieldData = FormFieldData(""),
    val isLoading: Boolean = false,
    val showFormInvalidToast: Boolean = false,
    val isDone: Boolean = false,
    val signupErrorResource: Int? = null,
)