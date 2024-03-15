package com.nestor.auth.data

import com.nestor.auth.data.datasource.AuthLocalDataSource
import com.nestor.auth.data.datasource.AuthRemoteDataSource
import com.nestor.database.data.user.UserEntity
import com.nestor.schema.UserDetailsQuery
import com.nestor.schema.adapter.UserDetailsQuery_ResponseAdapter
import com.nestor.schema.type.User
import com.nestor.schema.utils.ResponseWrapper
import com.nestor.schema.utils.safeApiCall
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import java.lang.IllegalStateException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val localDatasource: AuthLocalDataSource,
    private val remoteDataSource: AuthRemoteDataSource
) : AuthRepository {

    override suspend fun register(username: String, name: String, password: String) =
        safeApiCall {
            remoteDataSource.register(username = username, name = name, password = password)
        }

    override suspend fun login(
        username: String,
        password: String
    ) = safeApiCall { remoteDataSource.login(username, password) }

    override suspend fun forgotPassword(username: String) =
        safeApiCall { this.remoteDataSource.forgotPassword(username) }

    override suspend fun recoverPassword(newPassword: String) = safeApiCall {
        this.remoteDataSource.recoverPassword(newPassword)
    }

    override suspend fun updateUserCurrency(code: String) {
        this.localDatasource.updateUserCurrency(code)
        this.remoteDataSource.updateUserCurrency(code)
    }

    override fun userDetails() = flow {
        localDatasource.userDetails().collect {
            it?.let { user ->
                emit(ResponseWrapper.success<UserEntity?>(user))
            } ?: run {
                emit(ResponseWrapper.loading())
                if (localDatasource.rawToken() == null) {
                    emit(ResponseWrapper.success<UserEntity?>(null))
                    return@run
                }
                val result = safeApiCall { remoteDataSource.fetchUserDetails() }
                saveUserDetails(result)
                emit(ResponseWrapper.success(result.body?.userEntity()))
            }
        }
    }

    private suspend fun saveUserDetails(userResponse: ResponseWrapper<UserDetailsQuery.Data>) {
        userResponse.body?.let {
            localDatasource.storeUser(it.userEntity())
        } ?: {
            throw IllegalStateException("User call ended with: ${userResponse.error}")
        }
    }

    override fun getRawToken(): String? {
        return localDatasource.rawToken()
    }

    override suspend fun setRawToken(token: String) {
        localDatasource.storeToken(token)
    }
}

private fun UserDetailsQuery.Data.userEntity(): UserEntity {
    return UserEntity(
        name = this.userDetails.user.name,
        currencyCode = this.userDetails.user.currencyCode,
        email = this.userDetails.user.username,
        uuid = this.userDetails.user.id
    )
}