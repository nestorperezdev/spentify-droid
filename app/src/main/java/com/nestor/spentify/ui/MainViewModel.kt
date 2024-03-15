package com.nestor.spentify.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nestor.auth.data.AuthRepository
import com.nestor.auth.data.model.AuthState
import com.nestor.onboarding.data.datasource.OnboardingLocalDataSource
import com.nestor.schema.utils.ResponseWrapper
import com.nestor.uikit.util.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    authRepository: AuthRepository,
    private val onboardingLocalDataSource: OnboardingLocalDataSource,
    private val dispatcherProvider: CoroutineContextProvider
) : ViewModel() {
    val authState: StateFlow<ResponseWrapper<AuthState>> = authRepository.userDetails()
        .map {
            if (it.isLoading) ResponseWrapper.loading()
            else if (it.body != null) ResponseWrapper.success<AuthState>(AuthState.Authenticated(it.body!!))
            else ResponseWrapper.success<AuthState>(AuthState.Anonymous)
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, ResponseWrapper.loading())
    val tookOnboarding: StateFlow<Boolean> = onboardingLocalDataSource.isOnboardingComplete()
        .stateIn(viewModelScope, SharingStarted.Lazily, true)

    fun onOnboardingFinished() {
        viewModelScope.launch(dispatcherProvider.io()) {
            onboardingLocalDataSource.setOnBoardingComplete()
        }
    }
}