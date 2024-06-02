package com.nestor.auth.ui.login

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
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val coroutineContextProvider: CoroutineContextProvider
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()
    fun onEmailChanged(value: String) {
        val isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()
        val errorResource = if (isValid) null else R.string.enter_a_valid_email_address
        _uiState.update { it.copy(email = it.email.copy(value, errorResource)) }
    }

    fun onPasswordChange(value: String) {
        val isValid = value.length >= 6
        val errorResource = if (isValid) null else R.string.password_must_be_at_least_6_characters
        _uiState.update {
            it.copy(password = it.password.copy(value, errorResource))
        }
    }

    fun triggerValidation() {
        this.onEmailChanged(_uiState.value.email.value)
        this.onPasswordChange(_uiState.value.password.value)
    }

    fun isFormValid(): Boolean {
        this.triggerValidation()
        return _uiState.value.email.hasError.not() &&
                _uiState.value.password.hasError.not()
    }

    fun onSubmit() {
        if (this.isFormValid().not()) {
            _uiState.update {
                it.copy(
                    loginErrorResource = R.string.form_is_invalid_please_check_the_fields_and_try_again,
                    isLoading = false
                )
            }
            return
        }
        _uiState.update { it.copy(loginErrorResource = null, isLoading = true) }
        viewModelScope.launch(coroutineContextProvider.io()) {
            val loginResult =
                authRepository.login(_uiState.value.email.value, _uiState.value.password.value)
            loginResult.body?.login?.loginToken?.token?.let { token ->
                authRepository.setRawToken(token)
                _uiState.update { it.copy(isSuccess = true) }
            } ?: run {
                _uiState.update { it.copy(isLoading = false) }
                _uiState.update { it.copy(loginErrorResource = R.string.incorrect_password_or_email) }
            }
        }
    }

    fun onErrorToastDismissed() {
        _uiState.update { it.copy(loginErrorResource = null) }
    }
}
