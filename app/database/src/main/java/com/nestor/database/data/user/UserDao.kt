package com.nestor.database.data.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE user_uuid = :uuid")
    fun getUser(uuid: String): UserEntity
    @Insert
    fun insertUser(user: UserEntity)
    @Query("DELETE FROM user")
    fun clearUsers(): Int

    @Query("UPDATE user SET currency_code = :currencyCode")
    fun updateCurrency(currencyCode: String)
}
