package com.nestor.schema.utils

data class ResponseWrapper<T>(
    val isLoading: Boolean = false, val error: String? = null, val body: T? = null
) {
    companion object {
        fun <T> loading(): ResponseWrapper<T> = ResponseWrapper(isLoading = true)
        fun <T> success(body: T): ResponseWrapper<T> = ResponseWrapper(body = body)
        fun <T> error(error: String): ResponseWrapper<T> = ResponseWrapper(error = error)
    }
}

fun <R, T> ResponseWrapper<T>.mapBody(transform: (T) -> R): ResponseWrapper<R> {
    return when {
        isLoading -> ResponseWrapper.loading()
        error != null -> ResponseWrapper.error(error)
        else -> ResponseWrapper.success(transform(body!!))
    }
}

fun <R, T, Z> ResponseWrapper<T>.combineTransform(
    other: ResponseWrapper<R>,
    transform: (T, R) -> Z
): ResponseWrapper<Z> {
    return when {
        isLoading || other.isLoading -> ResponseWrapper.loading()
        error != null -> ResponseWrapper.error(error)
        other.error != null -> ResponseWrapper.error(other.error)
        body == null || other.body == null -> ResponseWrapper.error("Null body")
        else -> ResponseWrapper.success(transform(body, other.body))
    }
}
