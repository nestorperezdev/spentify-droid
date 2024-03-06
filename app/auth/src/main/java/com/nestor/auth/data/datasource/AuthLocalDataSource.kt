package com.nestor.auth.data.datasource

import com.nestor.auth.data.model.AuthState
import com.nestor.auth.data.model.TokenPayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AuthLocalDataSource {
    fun userDetails(): Flow<TokenPayload?>
    fun isUserLoggedIn(): StateFlow<AuthState>
    suspend fun storeUserToken(token: String, name: String, email: String)
}