package com.nestor.database.data.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user LIMIT 1")
    fun getUser(): Flow<UserEntity?>
    @Insert
    suspend fun insertUser(user: UserEntity)
    @Query("DELETE FROM user")
    fun clearUsers(): Int

    @Query("UPDATE user SET currency_code = :currencyCode")
    suspend fun updateCurrency(currencyCode: String)
}
