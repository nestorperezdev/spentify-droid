package com.nestor.database.data.expense

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity("expense")
data class ExpenseEntity(
    @PrimaryKey val id: String,
    @ColumnInfo("user_uuid") val userUuid: String,
    @ColumnInfo val description: String,
    @ColumnInfo val date: Date,
    @ColumnInfo("stored_at") val storedAt: Date,
    @ColumnInfo val amount: Double,
    @ColumnInfo val order: Int,
    @ColumnInfo("usd_value") val usdValue: Double
)