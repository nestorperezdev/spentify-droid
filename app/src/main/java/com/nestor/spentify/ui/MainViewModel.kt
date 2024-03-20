package com.nestor.spentify.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nestor.auth.data.AuthRepository
import com.nestor.auth.data.model.AuthState
import com.nestor.database.data.user.UserEntity
import com.nestor.onboarding.data.datasource.OnboardingLocalDataSource
import com.nestor.schema.utils.ResponseWrapper
import com.nestor.spentify.navigation.AppNavigationGraph
import com.nestor.uikit.util.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
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
    val navRoute: StateFlow<String> = combine(
        authRepository.userDetails(), onboardingLocalDataSource.isOnboardingComplete()
    ) { authState, onboardingState ->
        when {
            authState.isLoading -> AppNavigationGraph.Splash.route
            !onboardingState -> AppNavigationGraph.Onboarding.route
            authState.body is UserEntity -> AppNavigationGraph.Home.route
            authState.body !is UserEntity -> AppNavigationGraph.AuthGraph.route
            else -> AppNavigationGraph.Splash.route
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, AppNavigationGraph.Splash.route)

    fun onOnboardingFinished() {
        viewModelScope.launch(dispatcherProvider.io()) {
            onboardingLocalDataSource.setOnBoardingComplete()
        }
    }
}