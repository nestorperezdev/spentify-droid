package com.nestor.dashboard.data

import com.nestor.database.data.dashboard.DashboardEntity
import com.nestor.uikit.util.CoroutineContextProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDashboardDataSource,
    private val remoteDataSource: DashboardRemoteDataSource,
    private val coroutineProvider: CoroutineContextProvider
) : DashboardRepository {
    override fun fetchDashboardInfo(userUuid: String): Flow<DashboardEntity?> {
        return localDataSource.getCurrentDashboard(userUuid)
    }

    override fun refreshDashboardData() {
        runBlocking(coroutineProvider.io()) {
            remoteDataSource.fetchDashboardInfo()
        }
    }
}