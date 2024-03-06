package com.nestor.dashboard.data

import com.nestor.database.data.dashboard.DashboardEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class DashboardRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDashboardDataSource
) : DashboardRepository {
    override fun fetchDashboardInfo(): Flow<DashboardEntity> {
        return localDataSource.getCurrentDashboard("")
    }
}