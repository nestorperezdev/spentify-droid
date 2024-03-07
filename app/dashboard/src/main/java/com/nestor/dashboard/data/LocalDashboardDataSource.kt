package com.nestor.dashboard.data

import com.nestor.database.data.dashboard.DashboardEntity
import kotlinx.coroutines.flow.Flow

interface LocalDashboardDataSource {
    fun getCurrentDashboard(userUuid: String): Flow<DashboardEntity?>
}