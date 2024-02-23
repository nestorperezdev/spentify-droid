package com.nestor.spentify.ui

import androidx.lifecycle.ViewModel
import com.nestor.auth.data.AuthRepository
import com.nestor.auth.data.model.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(authRepository: AuthRepository): ViewModel() {
    val authState: StateFlow<AuthState> = authRepository.authState
}