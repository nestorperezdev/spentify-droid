package com.nestor.auth.data

import com.apollographql.apollo3.api.ApolloResponse
import com.nestor.auth.data.datasource.AuthLocalDataSource
import com.nestor.auth.data.datasource.AuthRemoteDataSource
import com.nestor.auth.data.model.AuthState
import com.nestor.auth.data.model.TokenPayload
import com.nestor.schema.ForgotPasswordMutation
import com.nestor.schema.LoginMutation
import com.nestor.schema.RecoverPasswordMutation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authLocalDataSource: AuthLocalDataSource,
    private val remoteDataSource: AuthRemoteDataSource
) : AuthRepository {
    override val authState: StateFlow<AuthState> = authLocalDataSource.isUserLoggedIn()

    override fun register(username: String, name: String, password: String) = flow {
        val result =
            remoteDataSource.register(username = username, name = name, password = password)
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

    override fun userLoginData(): Flow<TokenPayload?> {
        return this.authLocalDataSource.userDetails()
    }

    override suspend fun login(
        username: String,
        password: String
    ): ApolloResponse<LoginMutation.Data> {
        val loginResult = remoteDataSource.login(username, password)
        if (loginResult.hasErrors().not()) {
            loginResult.data?.let {
                authLocalDataSource.storeUserToken(
                    it.login.loginToken.token,
                    it.login.loginToken.name,
                    username
                )

            }
        }
        return loginResult
    }

    override suspend fun forgotPassword(username: String): ApolloResponse<ForgotPasswordMutation.Data> {
        return this.remoteDataSource.forgotPassword(username)
    }

    override suspend fun recoverPassword(newPassword: String): ApolloResponse<RecoverPasswordMutation.Data> {
        return this.remoteDataSource.recoverPassword(newPassword)
    }
}