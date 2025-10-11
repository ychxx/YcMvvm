package com.yc.ycmvvm.exception


/**
 *  网络请求异常类
 */
public interface IYcExceptionBase {
    var code: Int
    var msg: String?
}