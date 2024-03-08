package com.nestor.dashboard.data

import com.nestor.database.data.dashboard.DashboardDao
import com.nestor.database.data.dashboard.DashboardEntity
import javax.inject.Inject

class LocalDashboardDataSourceImpl @Inject constructor(private val dashboardDao: DashboardDao) :
    LocalDashboardDataSource {
    override fun getCurrentDashboard(userUuid: String) = dashboardDao.getDashboard(userUuid)
    override fun insertDashboard(dashboardEntity: DashboardEntity) =
        dashboardDao.insertDashboard(dashboardEntity)
}