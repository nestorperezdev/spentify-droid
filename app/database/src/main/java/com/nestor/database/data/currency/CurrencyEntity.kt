package com.nestor.database.data.currency

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "currency")
data class CurrencyEntity(
    @PrimaryKey val id: Int? = null,
    @ColumnInfo("code") val code: String,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("last_update") val lastUpdate: Date,
    @ColumnInfo("symbol") val symbol: String,
    @ColumnInfo("usd_rate") val usdRate: Double,
)