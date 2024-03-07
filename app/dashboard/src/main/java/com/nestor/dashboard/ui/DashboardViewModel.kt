package com.nestor.dashboard.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nestor.auth.data.AuthRepository
import com.nestor.dashboard.data.DashboardRepository
import com.nestor.uikit.util.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dashboardRepository: DashboardRepository,
    coroutineProvider: CoroutineContextProvider
) : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(coroutineProvider.io()) {
            authRepository.userLoginData().filterNotNull().collect {
                dashboardRepository.fetchDashboardInfo(it.sub).collect { entity ->
                    if (entity != null) {
                        _uiState.update {
                            it.copy(
                                name = entity.userName,
                                dailyPhrase = entity.dailyPhrase
                            )
                        }
                    } else {
                        refreshDashboardData()
                    }
                }
            }
        }
    }

    private fun refreshDashboardData() {
        dashboardRepository.refreshDashboardData()
    }
}