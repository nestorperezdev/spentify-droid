package com.nestor.auth.ui.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nestor.auth.R
import com.nestor.auth.data.AuthRepository
import com.nestor.schema.utils.unwrapErrors
import com.nestor.uikit.util.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val coroutineContextProvider: CoroutineContextProvider
) : ViewModel() {
    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()
    fun onEmailChanged(value: String) {
        val isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()
        val errorResource = if (isValid) null else R.string.enter_a_valid_email_address
        _uiState.update { it.copy(email = it.email.copy(value, errorResource)) }
    }

    fun triggerValidation() {
        this.onEmailChanged(_uiState.value.email.value)
    }

    fun isFormValid(): Boolean {
        this.triggerValidation()
        return _uiState.value.email.hasError.not()
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
        viewModelScope.launch(
            coroutineContextProvider.network {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        loginErrorResource = R.string.network_error
                    )
                }
            }
        ) {
            val forgotPasswordResponse =
                authRepository.forgotPassword(_uiState.value.email.value)
            _uiState.update { it.copy(isLoading = false) }
            if (forgotPasswordResponse.hasErrors()) {
                forgotPasswordResponse.unwrapErrors().forEach {
                    when (it) {
                        else -> _uiState.update { it.copy(loginErrorResource = R.string.unknown_error) }
                    }
                }
            } else {
                _uiState.update { it.copy(successResponse = true) }
            }
        }
    }

    fun onErrorToastDismissed() {
        _uiState.update { it.copy(loginErrorResource = null) }
    }
}
