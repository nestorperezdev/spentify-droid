package com.nestor.uikit.util

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class CoroutineContextProviderImpl @Inject constructor() : CoroutineContextProvider {
    override fun default() = Dispatchers.Default

    override fun io() = Dispatchers.IO

    override fun main() = Dispatchers.Main

    override fun unconfined() = Dispatchers.Unconfined

    override fun network(networkError: (Throwable) -> Unit) =
        io() + CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            networkError(throwable)
        }
}