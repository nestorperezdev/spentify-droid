package com.nestor.schema.query

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.nestor.schema.UserDetailsQuery
import com.nestor.schema.apollo.apolloClient

class UserDetails(private val client: ApolloClient = apolloClient) {
    suspend fun getUserDetails(): ApolloResponse<UserDetailsQuery.Data> {
        return this.client.query(UserDetailsQuery()).execute()
    }
}