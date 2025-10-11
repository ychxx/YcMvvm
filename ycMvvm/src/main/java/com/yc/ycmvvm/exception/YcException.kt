package com.yc.ycmvvm.exception


/**
 *  网络请求异常类
 */
public open class YcException : Exception, IYcExceptionBase {
   override var code: Int = 0

    /**
     * 已知异常，抛出异常时用
     */
    var knownCode: Int
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
        this.knownCode = code
    }

    constructor(msg: String?, code: Int, knownCode: Int = code) : super(msg) {
        this.msg = msg
        this.code = code
        this.knownCode = knownCode
    }

    constructor(code: Int, msg: String?) : super(msg) {
        this.msg = msg
        this.code = code
        this.knownCode = code
    }
}