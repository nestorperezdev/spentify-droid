package com.nestor.auth.data.datasource

import com.nestor.auth.data.model.AuthState
import kotlinx.coroutines.flow.StateFlow

interface AuthLocalDataSource {
    fun isUserLoggedIn(): StateFlow<AuthState>
    suspend fun storeUserToken(token: String, name: String, email: String)
}