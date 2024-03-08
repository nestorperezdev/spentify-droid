package com.nestor.dashboard.data

import com.nestor.database.data.dashboard.DashboardDao
import com.nestor.database.data.dashboard.DashboardEntity
import com.nestor.schema.type.SummaryDashboardInput
import com.nestor.schema.type.UserTimeZoneInput
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class LocalDashboardDataSourceImpl @Inject constructor(private val dashboardDao: DashboardDao) :
    LocalDashboardDataSource {
    override fun getCurrentDashboard(userUuid: String) = dashboardDao.getDashboard(userUuid)
    override fun insertDashboard(dashboardEntity: DashboardEntity) =
        dashboardDao.insertDashboard(dashboardEntity)

    override fun getSummaryContext(): SummaryDashboardInput {
        return SummaryDashboardInput(
            timeZoneData = UserTimeZoneInput(
                userTime = LocalDate.now().format(DateTimeFormatter.ISO_DATE),
                zoneFixedOffsetId = ZoneId.systemDefault().id
            )
        )
    }
}