package com.nestor.schema.utils

import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.exception.ApolloHttpException
import com.apollographql.apollo.exception.ApolloNetworkException
import java.net.SocketTimeoutException

suspend fun <T : Operation.Data> safeApiCall(call: suspend () -> ApolloResponse<T>): ResponseWrapper<T> {
    try {
        val response = call()
        response.exception?.let {
            return ResponseWrapper.error(response.errors?.firstOrNull()?.message ?: "Unknown error")
        }
        response.data?.let {
            return ResponseWrapper.success(it)
        }
        return ResponseWrapper.error("Body is empty!")
    } catch (e: SocketTimeoutException) {
        e.printStackTrace()
        return ResponseWrapper.error("Socket timeout exception")
    } catch (e: ApolloNetworkException) {
        e.printStackTrace()
        return ResponseWrapper.error("Apollo network exception")
    } catch (e: ApolloHttpException) {
        e.printStackTrace()
        return ResponseWrapper.error("Server error")
    }
}