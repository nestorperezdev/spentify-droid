package com.nestor.auth.data.datasource

import com.apollographql.apollo3.api.ApolloResponse
import com.nestor.schema.ForgotPasswordMutation
import com.nestor.schema.LoginMutation
import com.nestor.schema.RegisterMutation

interface AuthRemoteDataSource {
    suspend fun register(
        username: String,
        name: String,
        password: String
    ): ApolloResponse<RegisterMutation.Data>

    suspend fun login(username: String, password: String): ApolloResponse<LoginMutation.Data>
    suspend fun forgotPassword(username: String): ApolloResponse<ForgotPasswordMutation.Data>
}