package com.nestor.dashboard.data

import com.nestor.database.data.dashboard.DashboardEntity
import com.nestor.schema.utils.ResponseWrapper
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {
    fun fetchDashboardInfo(userUuid: String): Flow<ResponseWrapper<DashboardEntity>>
    fun refreshDashboardData()
}