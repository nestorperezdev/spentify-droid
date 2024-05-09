package com.nestor.account.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nestor.common.data.auth.AuthRepository
import com.nestor.uikit.util.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val coroutineContextProvider: CoroutineContextProvider
) :
    ViewModel() {
    fun logout() {
        viewModelScope.launch(coroutineContextProvider.io()) {
            authRepository.logout()
        }
    }
}