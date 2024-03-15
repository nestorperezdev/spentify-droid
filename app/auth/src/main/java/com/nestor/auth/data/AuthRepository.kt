package com.nestor.auth.data

import com.nestor.database.data.user.UserEntity
import com.nestor.schema.ForgotPasswordMutation
import com.nestor.schema.LoginMutation
import com.nestor.schema.RecoverPasswordMutation
import com.nestor.schema.RegisterMutation
import com.nestor.schema.utils.ResponseWrapper
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun register(username: String, name: String, password: String): ResponseWrapper<RegisterMutation.Data>
    suspend fun login(username: String, password: String): ResponseWrapper<LoginMutation.Data>
    suspend fun forgotPassword(username: String): ResponseWrapper<ForgotPasswordMutation.Data>
    suspend fun recoverPassword(newPassword: String): ResponseWrapper<RecoverPasswordMutation.Data>
    suspend fun updateUserCurrency(code: String)
    fun userDetails(): Flow<ResponseWrapper<UserEntity?>>
    fun getRawToken(): String?
    suspend fun setRawToken(token: String)
}
