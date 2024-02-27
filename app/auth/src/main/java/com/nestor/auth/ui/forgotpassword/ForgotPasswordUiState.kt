package com.nestor.auth.ui.forgotpassword

import com.nestor.uikit.input.FormFieldData

data class ForgotPasswordUiState(
    val email: FormFieldData = FormFieldData(""),
    val isLoading: Boolean = false,
    val loginErrorResource: Int? = null,
    val successResponse: Boolean = false,
)
