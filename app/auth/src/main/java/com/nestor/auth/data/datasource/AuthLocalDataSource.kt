package com.nestor.auth.data.datasource

import com.nestor.auth.data.model.AuthState
import com.nestor.auth.data.model.TokenPayload
import com.nestor.database.data.user.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AuthLocalDataSource {
    fun userDetailsFlow(): Flow<TokenPayload?>
    fun userDetails(): TokenPayload?
    fun isUserLoggedIn(): StateFlow<AuthState>
    fun getRawToken(): String?
    suspend fun storeUserToken(token: String, name: String, email: String)
    suspend fun getUserDetails(uuid: String): UserEntity?
    suspend fun storeUser(userEntity: UserEntity)
    suspend fun clearUsers()
}