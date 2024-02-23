package com.nestor.auth.data.datasource

import com.nestor.auth.data.model.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class AuthLocalDataSourceImpl @Inject constructor() : AuthLocalDataSource {
    private val _authState = MutableStateFlow(AuthState.ANONYMOUS)
    override fun isUserLoggedIn(): StateFlow<AuthState> = _authState
    override suspend fun storeUserToken(token: String, name: String, email: String) {
        _authState.update { AuthState.AUTHENTICATED }
    }
}
