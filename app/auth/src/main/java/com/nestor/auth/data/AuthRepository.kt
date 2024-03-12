package com.nestor.auth.data

import com.apollographql.apollo3.api.ApolloResponse
import com.nestor.auth.data.model.AuthState
import com.nestor.schema.ForgotPasswordMutation
import com.nestor.schema.LoginMutation
import com.nestor.schema.RecoverPasswordMutation
import com.nestor.schema.RegisterMutation
import com.nestor.schema.utils.ResponseWrapper
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun register(username: String, name: String, password: String): Flow<ApolloResponse<RegisterMutation.Data>>
    suspend fun login(username: String, password: String): ApolloResponse<LoginMutation.Data>
    suspend fun forgotPassword(username: String): ApolloResponse<ForgotPasswordMutation.Data>
    suspend fun recoverPassword(newPassword: String): ApolloResponse<RecoverPasswordMutation.Data>
    suspend fun updateUserCurrency(code: String)
    fun userDetails(): Flow<ResponseWrapper<AuthState>>
    fun getRawToken(): String?
}
