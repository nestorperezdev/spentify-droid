package com.nestor.database.data.dashboard

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class DashboardEntity(
    @PrimaryKey val id: Int? = null,
    @ColumnInfo("user_uuid") val userUuid: String,
    @ColumnInfo("name") val userName: String,
    @ColumnInfo("daily_phrase") val dailyPhrase: String?,
    @ColumnInfo("created_at") val createdAt: Date = Date(),
    @ColumnInfo("total_expenses") val totalExpenses: Double,
    @ColumnInfo("daily_average_expense") val dailyAverageExpense: Double,
    @ColumnInfo("maximal_expense") val maximalExpense: Double,
    @ColumnInfo("minimal_expense") val minimalExpense: Double,
)
