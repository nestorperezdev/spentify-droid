package com.nestor.auth.data

import com.nestor.auth.data.datasource.AuthLocalDataSource
import com.nestor.auth.data.datasource.AuthRemoteDataSource
import com.nestor.auth.data.model.AuthState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authLocalDataSource: AuthLocalDataSource,
    private val remoteDataSource: AuthRemoteDataSource
) : AuthRepository {
    override val authState: StateFlow<AuthState> = authLocalDataSource.isUserLoggedIn()

    override fun register(username: String, name: String, password: String) = flow {
        val result = remoteDataSource.register(username, name, password)
        if (result.hasErrors().not()) {
            with(result.data!!.register.loginToken) {
                authLocalDataSource.storeUserToken(
                    this.token,
                    this.name,
                    username
                )
            }
        }
        emit(result)
    }

    override suspend fun login(username: String, password: String) {
        TODO("Not yet implemented")
    }
}