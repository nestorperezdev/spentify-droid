package com.nestor.auth.data.datasource

import com.nestor.auth.data.model.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class AuthLocalDataSourceImpl @Inject constructor() : AuthLocalDataSource {
    override fun isUserLoggedIn(): StateFlow<AuthState> = MutableStateFlow(AuthState.ANONYMOUS)
    override suspend fun storeUserToken(token: String, name: String, email: String) {
        TODO("Not yet implemented")
    }
}
