package com.nestor.common.data.auth

import com.nestor.database.data.user.UserEntity
import com.nestor.schema.ForgotPasswordMutation
import com.nestor.schema.LoginMutation
import com.nestor.schema.RecoverPasswordMutation
import com.nestor.schema.RegisterMutation
import com.nestor.schema.utils.ResponseWrapper
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun register(username: String, name: String, password: String): com.nestor.schema.utils.ResponseWrapper<RegisterMutation.Data>
    suspend fun login(username: String, password: String): com.nestor.schema.utils.ResponseWrapper<LoginMutation.Data>
    suspend fun forgotPassword(username: String): com.nestor.schema.utils.ResponseWrapper<ForgotPasswordMutation.Data>
    suspend fun recoverPassword(newPassword: String): com.nestor.schema.utils.ResponseWrapper<RecoverPasswordMutation.Data>
    fun userDetails(): Flow<com.nestor.schema.utils.ResponseWrapper<UserEntity?>>
    fun getRawToken(): String?
    suspend fun setRawToken(token: String)
    suspend fun logout()
}