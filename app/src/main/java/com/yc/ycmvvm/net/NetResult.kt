package com.yc.ycmvvm.net

import com.yc.ycmvvm.exception.YcException
import com.yc.ycmvvm.extension.ycToNoEmpty

open class NetResult<T> {
    var msg: String? = ""
    var code: Int = 0
    var data: T? = null
}

fun <T> NetResult<T>.ycGetMsg(): String {
    return msg.ycToNoEmpty("")
}

/**
 * 检测code 且data许空
 */
fun <T> NetResult<T>.checkCode(errorMsg: String = ycGetMsg()): T? {
    if (code != 200) {
        throw YcException(errorMsg, code)
    } else {
        return data
    }
}