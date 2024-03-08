package com.nestor.schema.utils

data class ResponseWrapper<T>(
    val isLoading: Boolean = false,
    val error: String? = null,
    val body: T? = null
)
