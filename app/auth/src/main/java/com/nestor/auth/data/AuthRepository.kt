package com.nestor.auth.data

import com.apollographql.apollo3.api.ApolloResponse
import com.nestor.auth.data.model.AuthState
import com.nestor.auth.data.model.TokenPayload
import com.nestor.database.data.user.UserEntity
import com.nestor.schema.ForgotPasswordMutation
import com.nestor.schema.LoginMutation
import com.nestor.schema.RecoverPasswordMutation
import com.nestor.schema.RegisterMutation
import com.nestor.schema.utils.ResponseWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val authState: StateFlow<AuthState>
    fun register(username: String, name: String, password: String): Flow<ApolloResponse<RegisterMutation.Data>>
    fun userLoginData(): Flow<TokenPayload?>
    suspend fun login(username: String, password: String): ApolloResponse<LoginMutation.Data>
    suspend fun forgotPassword(username: String): ApolloResponse<ForgotPasswordMutation.Data>
    suspend fun recoverPassword(newPassword: String): ApolloResponse<RecoverPasswordMutation.Data>
    fun userDetails(): Flow<ResponseWrapper<UserEntity?>>
    suspend fun updateUserCurrency(code: String)
}
