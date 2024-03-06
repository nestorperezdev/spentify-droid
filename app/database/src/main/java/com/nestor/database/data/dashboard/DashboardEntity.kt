package com.nestor.database.data.dashboard

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class DashboardEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo("user_uuid") val userUuid: String,
    @ColumnInfo("name") val userName: String,
    @ColumnInfo("daily_phrase") val dailyPhrase: String?,
    @ColumnInfo("created_at", defaultValue = "CURRENT_TIMESTAMP") val createdAt: Date
)
