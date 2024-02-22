package com.nestor.auth.signup

import androidx.lifecycle.ViewModel
import com.nestor.login.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignupViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState: StateFlow<SignupUiState> = _uiState.asStateFlow()

    fun onNameChanged(value: String) {
        val isValid = value.length in 3..50
        val errorResource = if (isValid) null else R.string.name_max_and_min_chars_error
        _uiState.update { it.copy(name = it.name.copy(value, errorResource)) }
    }

    fun onEmailChanged(value: String) {
        val isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()
        val errorResource = if (isValid) null else R.string.enter_a_valid_email_address
        _uiState.update { it.copy(email = it.email.copy(value, errorResource)) }
    }

    /**
     * Password should contain at least 8 characters and 1 uppercase letter
     */
    fun onPasswordChange(value: String) {
        val isValid = value.length in 8..50 && value.any { it.isUpperCase() }
        val errorResource =
            if (isValid) null else R.string.password_must_contain_at_least_8_characters_and_1_uppercase_letter
        _uiState.update { it.copy(password = it.password.copy(value, errorResource)) }
    }

    fun onPasswordRepeatChange(value: String) {
        val isValid = value == _uiState.value.password.value
        val errorResource = if (isValid) null else R.string.passwords_do_not_match
        _uiState.update { it.copy(repeatPassword = it.repeatPassword.copy(value, errorResource)) }
    }

    fun onSubmit() {
        TODO("Not yet implemented")
    }
}