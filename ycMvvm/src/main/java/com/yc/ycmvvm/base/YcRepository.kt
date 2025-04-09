package com.yc.ycmvvm.base

import com.yc.ycmvvm.exception.YcException
import com.yc.ycmvvm.net.YcResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

abstract class YcRepository {
    abstract suspend fun errHandle(cause: Throwable): YcException?

    fun <T> ycFlow(
        errHandle: suspend (cause: Throwable) -> YcException? = this::errHandle,
        block: suspend FlowCollector<YcResult<T>>.() -> Unit
    ): Flow<YcResult<T>> = flow {
        block()
    }.catch {
        it.printStackTrace()
        val result = errHandle.invoke(it)
        if (result != null) {
            emit(YcResult.Fail(result))
        }
    }.flowOn(Dispatchers.IO)
}