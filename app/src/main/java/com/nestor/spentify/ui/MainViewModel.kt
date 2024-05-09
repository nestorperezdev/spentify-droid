package com.nestor.spentify.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nestor.common.data.auth.AuthRepository
import com.nestor.common.data.statusbar.StatusBarRepository
import com.nestor.database.data.user.UserEntity
import com.nestor.onboarding.data.repository.OnboardingRepository
import com.nestor.spentify.navigation.AppNavigationGraph
import com.nestor.uikit.statusbar.StatusBarType
import com.nestor.uikit.util.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    authRepository: AuthRepository,
    private val onboardingRepository: OnboardingRepository,
    private val dispatcherProvider: CoroutineContextProvider,
    statusBarRepository: StatusBarRepository
) : ViewModel() {
    val navRoute: StateFlow<String> = combine(
        authRepository.userDetails(), onboardingRepository.isOnboardingComplete()
    ) { authState, onboardingState ->
        when {
            authState.isLoading -> AppNavigationGraph.Splash.route
            !onboardingState -> AppNavigationGraph.Onboarding.route
            authState.body is UserEntity -> AppNavigationGraph.Home.route
            authState.body !is UserEntity -> AppNavigationGraph.AuthGraph.route
            else -> AppNavigationGraph.Splash.route
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, AppNavigationGraph.Splash.route)

    val statusBarType: StateFlow<StatusBarType?> = statusBarRepository.statusBarType()

    fun onOnboardingFinished() {
        viewModelScope.launch(dispatcherProvider.io()) {
            onboardingRepository.setOnBoardingComplete()
        }
    }
}