package com.nestor.auth.data

import com.apollographql.apollo3.api.ApolloResponse
import com.nestor.auth.data.model.AuthState
import com.nestor.schema.LoginMutation
import com.nestor.schema.RegisterMutation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val authState: StateFlow<AuthState>
    fun register(username: String, name: String, password: String): Flow<ApolloResponse<RegisterMutation.Data>>
    suspend fun login(username: String, password: String): ApolloResponse<LoginMutation.Data>
}
