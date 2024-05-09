package com.nestor.common.data.auth.datasource

import com.nestor.common.data.auth.model.TokenPayload
import com.nestor.database.data.user.UserEntity
import kotlinx.coroutines.flow.SharedFlow

interface AuthLocalDataSource {
    suspend fun clearUsers()
    suspend fun updateUserCurrency(currencyCode: String)
    suspend fun storeToken(token: String)
    suspend fun storeUser(userEntity: UserEntity)
    fun tokenContents(): SharedFlow<TokenPayload?>
    fun userDetails(): SharedFlow<UserEntity?>
    fun rawToken(): String?
    suspend fun clearToken()
}