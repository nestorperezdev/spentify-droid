package com.nestor.dashboard.data

import com.nestor.database.data.dashboard.DashboardEntity
import com.nestor.schema.type.SummaryDashboardInput
import kotlinx.coroutines.flow.Flow

interface LocalDashboardDataSource {
    fun getCurrentDashboard(userUuid: String): Flow<DashboardEntity?>
    fun insertDashboard(dashboardEntity: DashboardEntity)
    fun getSummaryContext(): SummaryDashboardInput
}