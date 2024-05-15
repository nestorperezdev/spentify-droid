package com.nestor.database.data.expense

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class ExpenseEntity(
    @PrimaryKey
    val id: Int? = null,
    @ColumnInfo("user_uuid") val userUuid: String,
    @ColumnInfo val description: String,
    @ColumnInfo val date: Date,
    @ColumnInfo("stored_at") val storedAt: Date,
    @ColumnInfo val amount: Double,
    @ColumnInfo val cursor: Int,
    @ColumnInfo("usd_value") val usdValue: Double
)