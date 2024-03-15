package com.nestor.database.data.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val id: Int? = null,
    @ColumnInfo("user_uuid") val uuid: String,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("currency_code") val currencyCode: String,
    @ColumnInfo("username") val email: String,
)
