package com.nestor.common.data.auth

import com.nestor.common.data.auth.datasource.AuthLocalDataSource
import com.nestor.common.data.auth.datasource.AuthRemoteDataSource
import com.nestor.database.data.user.UserEntity
import com.nestor.schema.UserDetailsQuery
import com.nestor.schema.utils.ResponseWrapper
import com.nestor.schema.utils.safeApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
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

    override fun userDetails(): Flow<ResponseWrapper<UserEntity?>> =
        localDatasource.tokenContents().flatMapLatest { tokenPayload ->
            localDatasource.userDetails().map {
                it?.let { user ->
                    ResponseWrapper.success(user)
                } ?: run {
                    ResponseWrapper.loading<UserEntity?>()
                    if (tokenPayload == null) {
                        ResponseWrapper.success(null)
                    } else {
                        val result = safeApiCall { remoteDataSource.fetchUserDetails() }
                        saveUserDetails(result)
                        ResponseWrapper.success(result.body?.userEntity())
                    }
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

    override suspend fun logout() {
        localDatasource.clearUsers()
        localDatasource.clearToken()
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