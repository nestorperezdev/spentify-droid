package com.nestor.auth.ui.recoverpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nestor.auth.R
import com.nestor.common.data.auth.AuthRepository
import com.nestor.uikit.util.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecoverPasswordViewModel @Inject constructor(
    val authRepository: AuthRepository,
    val coroutineContextProvider: CoroutineContextProvider
) : ViewModel() {
    private val _uiState = MutableStateFlow(RecoverPasswordUIState())
    val uiState = _uiState.asStateFlow()

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

    fun onShowFormInvalidToastDismissed() {
        _uiState.update { it.copy(showFormInvalidToast = false) }
    }

    fun triggerValidation() {
        this.onPasswordChange(_uiState.value.password.value)
        this.onPasswordRepeatChange(_uiState.value.repeatPassword.value)
    }

    fun isFormValid(): Boolean {
        this.triggerValidation()
        return _uiState.value.password.hasError.not() &&
                _uiState.value.repeatPassword.hasError.not()
    }

    fun onSubmit() {
        if (this.isFormValid().not()) {
            _uiState.update { it.copy(showFormInvalidToast = true) }
            return
        }
        _uiState.update { it.copy(isLoading = true, signupErrorResource = null) }
        viewModelScope.launch(coroutineContextProvider.io()) {
            val result = authRepository.recoverPassword(newPassword = _uiState.value.password.value)
            _uiState.update { it.copy(isLoading = false) }
            if (result.error != null) {
                _uiState.update { it.copy(signupErrorResource = R.string.unknown_error) }
            } else {
                _uiState.update { it.copy(isDone = true) }
                result.body?.recoverPassword?.loginToken?.token?.let {
                    authRepository.setRawToken(it)
                }
            }
        }
    }
}