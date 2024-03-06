package com.nestor.dashboard.data

import com.nestor.database.data.dashboard.DashboardEntity
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {
    fun fetchDashboardInfo(): Flow<DashboardEntity>
}