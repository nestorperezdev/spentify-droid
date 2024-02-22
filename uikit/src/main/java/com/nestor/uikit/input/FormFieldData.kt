package com.nestor.uikit.input

data class FormFieldData(
    val value: String,
    val errorResource: Int? = null,
) {
    val hasError: Boolean
        get() = errorResource != null
}
