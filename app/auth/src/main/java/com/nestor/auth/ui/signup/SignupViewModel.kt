package com.nestor.auth.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nestor.auth.R
import com.nestor.common.data.auth.AuthRepository
import com.nestor.uikit.util.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val coroutineContextProvider: CoroutineContextProvider
) : ViewModel() {
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

    fun triggerValidation() {
        this.onNameChanged(_uiState.value.name.value)
        this.onEmailChanged(_uiState.value.email.value)
        this.onPasswordChange(_uiState.value.password.value)
        this.onPasswordRepeatChange(_uiState.value.repeatPassword.value)
    }

    fun isFormValid(): Boolean {
        this.triggerValidation()
        return _uiState.value.name.hasError.not() &&
                _uiState.value.email.hasError.not() &&
                _uiState.value.password.hasError.not() &&
                _uiState.value.repeatPassword.hasError.not()
    }

    fun onSubmit() {
        if (this.isFormValid().not()) {
            _uiState.update { it.copy(showFormInvalidToast = true) }
            return
        }
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(coroutineContextProvider.io()) {
            val result = authRepository.register(
                name = _uiState.value.name.value,
                username = _uiState.value.email.value,
                password = _uiState.value.password.value
            )
            result.body?.register?.loginToken?.token?.let {
                authRepository.setRawToken(it)
                _uiState.update { it.copy(isSuccess = true) }
            } ?: run {
                _uiState.update { it.copy(signupErrorResource = R.string.unknown_error) }
            }
        }
    }

    fun onShowFormInvalidToastDismissed() {
        _uiState.update { it.copy(showFormInvalidToast = false) }
    }
}