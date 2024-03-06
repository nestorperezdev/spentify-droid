package com.nestor.dashboard.data

import com.nestor.database.data.dashboard.DashboardDao
import javax.inject.Inject

internal class LocalDashboardDataSource @Inject constructor(private val dashboardDao: DashboardDao) {
    fun getCurrentDashboard(userUuid: String) = dashboardDao.getDashboard(userUuid)
}