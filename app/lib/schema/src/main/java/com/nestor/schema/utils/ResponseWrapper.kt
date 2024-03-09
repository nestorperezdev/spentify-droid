package com.nestor.schema.utils

data class ResponseWrapper<T>(
    val isLoading: Boolean = false,
    val error: String? = null,
    val body: T? = null
) {
    companion object {
        fun <T> loading(): ResponseWrapper<T> = ResponseWrapper(isLoading = true)
        fun <T> success(body: T): ResponseWrapper<T> = ResponseWrapper(body = body)
        fun <T> error(error: String): ResponseWrapper<T> = ResponseWrapper(error = error)
    }
}
