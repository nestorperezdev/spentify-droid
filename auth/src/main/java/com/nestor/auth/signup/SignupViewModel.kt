package com.nestor.auth.signup

import androidx.lifecycle.ViewModel
import com.nestor.login.R
import com.nestor.uikit.input.FormFieldData
import com.nestor.uikit.input.FormStatus
import com.nestor.uikit.input.validate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignupViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        SignupUiState(
            name = FormFieldData(
                value = "",
                validationFunctions = listOf(::isValidName),
                status = FormStatus()
            ),
            email = FormFieldData(
                value = "",
                validationFunctions = listOf(::isEmail),
                status = FormStatus()
            ),
            password = FormFieldData(
                value = "",
                validationFunctions = emptyList(),
                status = FormStatus()
            ),
            repeatPassword = FormFieldData(
                value = "",
                validationFunctions = emptyList(),
                status = FormStatus()
            )
        )
    )
    val uiState: StateFlow<SignupUiState> = _uiState.asStateFlow()

    init {
        _uiState.value.name.onValueUpdated = { name ->
            _uiState.update {
                it.copy(
                    name = it.name.copy(
                        value = name,
                        status = validate(name, it.name.validationFunctions)
                    ),
                )
            }
        }

        _uiState.value.email.onValueUpdated = { email ->
            _uiState.update {
                it.copy(
                    email = it.email.copy(
                        value = email,
                        status = validate(email, it.email.validationFunctions)
                    )
                )
            }
        }

        _uiState.value.password.onValueUpdated = ::onPasswordChanged
        _uiState.value.repeatPassword.onValueUpdated = ::onRepeatPasswordChanged
    }

    /**
     * Only valid emails are allowed.
     */
    private fun isEmail(value: String): FormStatus {
        val isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()
        return FormStatus(
            isError = isValid.not(),
            errorResource = if (isValid.not()) {
                R.string.enter_a_valid_email_address
            } else {
                null
            }
        )
    }

    /**
     * At least 3 characters are required.
     * Maximum 50 characters are allowed.
     */
    fun isValidName(value: String): FormStatus {
        val isValid = value.length in 3..50
        return FormStatus(
            isError = isValid.not(),
            errorResource = if (isValid.not()) {
                R.string.name_max_and_min_chars_error
            } else {
                null
            }
        )
    }

    /**
     * Password requirements:
     * - At least 8 characters
     * - At least 1 uppercase letter
     */
    fun onPasswordChanged(value: String) {
        val isValid = value.length >= 8 && value.any { it.isUpperCase() }
        val status = FormStatus(
            isError = isValid.not(),
            errorResource = if (isValid.not()) {
                R.string.password_must_contain_at_least_8_characters_and_1_uppercase_letter
            } else {
                null
            }
        )
        _uiState.update {
            it.copy(
                password = it.password.copy(
                    value = value,
                    status = status
                )
            )
        }
        this.onRepeatPasswordChanged(_uiState.value.repeatPassword.value)
    }

    /**
     * Value should match the value of the password field.
     */
    fun onRepeatPasswordChanged(value: String) {
        val isValid = value == _uiState.value.password.value
        val status = FormStatus(
            isError = isValid.not(),
            errorResource = if (isValid.not()) {
                R.string.passwords_do_not_match
            } else {
                null
            }
        )
        _uiState.update {
            it.copy(
                repeatPassword = it.repeatPassword.copy(
                    value = value,
                    status = status
                ),
            )
        }
    }

    fun onSubmit() {
        TODO("Not yet implemented")
    }
}