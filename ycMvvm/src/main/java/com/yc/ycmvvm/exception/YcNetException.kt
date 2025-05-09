package com.yc.ycmvvm.exception

import java.io.IOException


/**
 *  网络请求异常类
 */
public open class YcNetException : IOException, IYcExceptionBase {
    override var code: Int = 0
    override var msg: String? = null
        get() {
            return if (field == null) {
                ""
            } else {
                field
            }
        }

    constructor(code: Int, throwable: Throwable?) : super(throwable) {
        this.code = code
    }

    constructor(msg: String?, code: Int) : super(msg) {
        this.msg = msg
        this.code = code
    }
}