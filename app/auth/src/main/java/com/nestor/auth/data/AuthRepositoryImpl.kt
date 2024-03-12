package com.nestor.auth.data

import com.apollographql.apollo3.api.ApolloResponse
import com.nestor.auth.data.datasource.AuthLocalDataSource
import com.nestor.auth.data.datasource.AuthRemoteDataSource
import com.nestor.auth.data.model.AuthState
import com.nestor.database.data.user.UserEntity
import com.nestor.schema.ForgotPasswordMutation
import com.nestor.schema.LoginMutation
import com.nestor.schema.RecoverPasswordMutation
import com.nestor.schema.utils.ResponseWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val localDatasource: AuthLocalDataSource,
    private val remoteDataSource: AuthRemoteDataSource
) : AuthRepository {

    override fun register(username: String, name: String, password: String) = flow {
        val result =
            remoteDataSource.register(username = username, name = name, password = password)
        result.data?.register?.loginToken?.token?.let { token -> localDatasource.storeToken(token) }
        emit(result)
    }

    override suspend fun login(
        username: String,
        password: String
    ): ApolloResponse<LoginMutation.Data> {
        val loginResult = remoteDataSource.login(username, password)
        loginResult.data?.login?.loginToken?.token?.let { token -> localDatasource.storeToken(token) }
        return loginResult
    }

    override suspend fun forgotPassword(username: String): ApolloResponse<ForgotPasswordMutation.Data> {
        return this.remoteDataSource.forgotPassword(username)
    }

    override suspend fun recoverPassword(newPassword: String): ApolloResponse<RecoverPasswordMutation.Data> {
        return this.remoteDataSource.recoverPassword(newPassword)
    }

    override suspend fun updateUserCurrency(code: String) {
        this.localDatasource.updateUserCurrency(code)
        this.remoteDataSource.updateUserCurrency(code)
    }

    override fun userDetails(): Flow<ResponseWrapper<AuthState>> = flow {
        localDatasource.tokenContents().collect {
            it?.let { _ ->
                localDatasource.userDetails().collect {
                    it?.let { details ->
                        emit(ResponseWrapper.success(AuthState.Authenticated(details)))
                    } ?: run {
                        val response = remoteDataSource.fetchUserDetails()
                        if (response.hasErrors()) {
                            emit(
                                ResponseWrapper.error(
                                    response.errors?.firstOrNull()?.message ?: "Unknown error"
                                )
                            )
                        } else {
                            response.data?.userDetails?.user?.let { user ->
                                localDatasource.storeUser(
                                    UserEntity(
                                        uuid = user.id,
                                        email = user.username,
                                        name = user.name,
                                        currencyCode = user.currencyCode
                                    )
                                )
                            }
                        }
                    }
                }
            } ?: run { emit(ResponseWrapper.success(AuthState.Anonymous)) }
        }
    }

    override fun getRawToken(): String? {
        return localDatasource.rawToken()
    }
}