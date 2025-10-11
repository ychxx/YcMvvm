package com.yc.ycmvvm.exception

class YcTokenException : YcNetException {
    constructor(code: Int, throwable: Throwable?) : super(code, throwable)
    constructor(msg: String?, code: Int) : super(msg, code)
}