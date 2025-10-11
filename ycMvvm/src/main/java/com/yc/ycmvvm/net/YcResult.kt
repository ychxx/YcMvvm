package com.yc.ycmvvm.net

import com.yc.ycmvvm.exception.IYcExceptionBase
import com.yc.ycmvvm.exception.YcException

/**
 *
 */
sealed class YcResult<out T> {
    data class Success<out T>(val data: T) : YcResult<T>()
    data class Fail(val exception: YcException) : YcResult<Nothing>()
}

inline fun <reified T> YcResult<T>.doSuccess(crossinline success: (T) -> Unit): YcResult<T> {
    if (this is YcResult.Success) {
        success(data)
    }
    return this
}

inline fun <reified T> YcResult<T>.doFail(crossinline failure: (IYcExceptionBase) -> Unit): YcResult<T> {
    if (this is YcResult.Fail) {
        failure(exception)
    }
    return this
}
