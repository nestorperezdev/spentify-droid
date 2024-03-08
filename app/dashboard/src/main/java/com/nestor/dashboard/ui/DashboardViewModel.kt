package com.nestor.dashboard.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nestor.auth.data.AuthRepository
import com.nestor.dashboard.data.DashboardRepository
import com.nestor.database.data.dashboard.DashboardEntity
import com.nestor.schema.utils.ResponseWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    authRepository: AuthRepository,
    private val dashboardRepository: DashboardRepository,
) : ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    val dashboardInfo: StateFlow<ResponseWrapper<DashboardEntity>> = authRepository.userLoginData()
        .filterNotNull()
        .flatMapLatest {
            dashboardRepository.fetchDashboardInfo(
                it.sub
            )
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, ResponseWrapper(isLoading = true))
}