package com.nestor.schema.apollo

import com.apollographql.apollo3.ApolloClient

val apolloClient = ApolloClient.Builder()
    .serverUrl("http://localhost:3000/graphql")
    .build()