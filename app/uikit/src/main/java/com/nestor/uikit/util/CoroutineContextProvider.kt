package com.nestor.uikit.util

import kotlin.coroutines.CoroutineContext

interface CoroutineContextProvider {
    fun default(): CoroutineContext
    fun io(): CoroutineContext
    fun main(): CoroutineContext
    fun unconfined(): CoroutineContext
    fun network(networkError: (Throwable) -> Unit = {}): CoroutineContext
}