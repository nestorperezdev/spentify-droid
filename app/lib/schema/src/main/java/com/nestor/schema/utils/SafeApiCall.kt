package com.nestor.schema.utils

import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.exception.ApolloNetworkException
import java.net.SocketTimeoutException

suspend fun <T : Operation.Data> safeApiCall(call: suspend () -> ApolloResponse<T>): ResponseWrapper<T> {
    try {
        val response = call()
        if (response.hasErrors()) {
            return ResponseWrapper.error(response.errors?.firstOrNull()?.message ?: "Unknown error")
        }
        response.data?.let {
            return ResponseWrapper.success(it)
        }
        return ResponseWrapper.error("Body is empty!")
    } catch (e: SocketTimeoutException) {
        return ResponseWrapper.error("Socket timeout exception")
    } catch (e: ApolloNetworkException) {
        return ResponseWrapper.error("Apollo network exception")
    }
}