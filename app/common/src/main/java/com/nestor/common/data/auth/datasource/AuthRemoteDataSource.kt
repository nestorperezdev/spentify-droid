package com.nestor.common.data.auth.datasource

import com.apollographql.apollo.api.ApolloResponse
import com.nestor.schema.ForgotPasswordMutation
import com.nestor.schema.LoginMutation
import com.nestor.schema.RecoverPasswordMutation
import com.nestor.schema.RegisterMutation
import com.nestor.schema.UserDetailsQuery

interface AuthRemoteDataSource {
    suspend fun register(
        username: String,
        name: String,
        password: String
    ): ApolloResponse<RegisterMutation.Data>

    suspend fun login(username: String, password: String): ApolloResponse<LoginMutation.Data>
    suspend fun forgotPassword(username: String): ApolloResponse<ForgotPasswordMutation.Data>
    suspend fun recoverPassword(newPassword: String): ApolloResponse<RecoverPasswordMutation.Data>
    suspend fun fetchUserDetails(): ApolloResponse<UserDetailsQuery.Data>
    suspend fun updateUserCurrency(currencyCode: String)
}