package com.nestor.common.data.auth.datasource

import com.apollographql.apollo3.api.http.HttpRequest
import com.apollographql.apollo3.api.http.HttpResponse
import com.apollographql.apollo3.exception.ApolloNetworkException
import com.apollographql.apollo3.network.http.HttpInterceptorChain
import okio.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class AuthTokenInterceptorImpl @Inject constructor(
    private val authRepository: AuthLocalDataSource
) : AuthTokenInterceptor {
    override suspend fun intercept(
        request: HttpRequest,
        chain: HttpInterceptorChain
    ): HttpResponse {
        val token = authRepository.rawToken()
        val newRequest = request.newBuilder()
        token?.let {
            newRequest.addHeader("Authorization", "Bearer $it")
        }
        return chain.proceed(newRequest.build())
    }
}