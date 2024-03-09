package com.nestor.common.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow

inline fun <T> Flow<T>.peak(crossinline peak: suspend FlowCollector<T>.(value: T) -> Unit): Flow<T> =
    flow {
        collect {
            peak(it)
            emit(it)
        }
    }