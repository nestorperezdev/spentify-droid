package com.nestor.common.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow

inline fun <E, T : List<E>> Flow<T>.onEachEmpty(crossinline peak: suspend FlowCollector<T>.(value: T) -> Unit): Flow<T> =
    flow {
        collect {
            if (it.isEmpty()) peak(it)
            emit(it)
        }
    }