package com.nestor.database.data.dashboard

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface DashboardDao {
    @Query("SELECT * FROM dashboardentity WHERE user_uuid = :userUuid and created_at >= created_at  ORDER BY created_at DESC LIMIT 1")
    fun getDashboard(
        userUuid: String,
        createdAt: Date = Date(System.currentTimeMillis() - (12 * 3600 * 1000))
    ): Flow<DashboardEntity>

    @Insert
    fun insertDashboard(dashboardEntity: DashboardEntity)
}