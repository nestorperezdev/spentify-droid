package com.nestor.uikit.input

data class FormFieldData(
    val value: String,
    var onValueUpdated: (String) -> Unit = {},
    val validationFunctions: List<(String) -> FormStatus?> = emptyList(),
    val status: FormStatus = validate(value, validationFunctions)
)

fun validate(
    value: String,
    validationFunctions: List<(String) -> FormStatus?> = emptyList()
): FormStatus {
    for (validationFunction in validationFunctions) {
        val status = validationFunction(value)
        if (status != null) {
            return status
        }
    }
    return FormStatus()
}

data class FormStatus(val isError: Boolean = false, val errorResource: Int? = null)
