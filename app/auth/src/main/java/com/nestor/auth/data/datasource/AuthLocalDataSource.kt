package com.nestor.auth.data.datasource

import com.nestor.auth.data.model.TokenPayload
import com.nestor.database.data.user.UserEntity
import kotlinx.coroutines.flow.Flow

interface AuthLocalDataSource {
    suspend fun clearUsers()
    suspend fun updateUserCurrency(currencyCode: String)
    suspend fun storeToken(token: String)
    suspend fun storeUser(userEntity: UserEntity)
    fun tokenContents(): Flow<TokenPayload?>
    fun userDetails(): Flow<UserEntity?>
    fun rawToken(): String?
}