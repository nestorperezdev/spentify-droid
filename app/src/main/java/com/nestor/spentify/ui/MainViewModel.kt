package com.nestor.spentify.ui

import androidx.lifecycle.ViewModel
import com.nestor.auth.data.AuthRepository
import com.nestor.auth.data.model.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(authRepository: AuthRepository) : ViewModel() {
    val authState: StateFlow<AuthState> = authRepository.authState
    private val _tookOnboarding = MutableStateFlow(false)
    val tookOnboarding: StateFlow<Boolean> = _tookOnboarding.asStateFlow()

    fun onOnboardingFinished() {
        _tookOnboarding.value = true
    }
}