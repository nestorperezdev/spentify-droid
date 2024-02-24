package com.nestor.schema.utils

import com.apollographql.apollo3.api.ApolloResponse

fun ApolloResponse<*>.unwrapErrors(): List<String> {
    return errors
        ?.filter { it.extensions?.containsKey("code") ?: false }
        ?.map { it.extensions!!["code"]!! as String } ?: emptyList()
}