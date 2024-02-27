package com.nestor.auth.data.datasource

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.nestor.schema.ForgotPasswordMutation
import com.nestor.schema.LoginMutation
import com.nestor.schema.RecoverPasswordMutation
import com.nestor.schema.RegisterMutation
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(val apolloClient: ApolloClient) :
    AuthRemoteDataSource {
    override suspend fun register(username: String, name: String, password: String) =
        apolloClient.mutation(
            RegisterMutation(
                username = username,
                name = name,
                password = password
            )
        ).execute()


    override suspend fun login(username: String, password: String) = apolloClient.mutation(
        LoginMutation(username, password)
    ).execute()

    override suspend fun forgotPassword(username: String): ApolloResponse<ForgotPasswordMutation.Data> {
        return this.apolloClient.mutation(ForgotPasswordMutation(username)).execute()
    }

    override suspend fun recoverPassword(newPassword: String): ApolloResponse<RecoverPasswordMutation.Data> {
        return this.apolloClient.mutation(RecoverPasswordMutation(newPassword)).execute()
    }
}