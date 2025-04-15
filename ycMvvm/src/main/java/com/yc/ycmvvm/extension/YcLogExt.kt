package com.yc.ycmvvm.extension

import android.util.Log
import com.elvishew.xlog.XLog

/**
 * log
 */
object YcLogExt {
    var mIsShowLogger: Boolean = true
}

fun ycLogESimple(msg: String? = "", tag: String = "ycLog") {
    if (YcLogExt.mIsShowLogger) {
        Log.e(tag, msg.ycToNoEmpty())
    }
}

fun ycLogE(msg: String? = "") {
    if (YcLogExt.mIsShowLogger) {
        XLog.e(msg.ycToNoEmpty())
    }
}

fun ycLogDSimple(msg: String? = "", tag: String = "ycLog") {
    if (YcLogExt.mIsShowLogger) {
        Log.d(tag, msg.ycToNoEmpty())
    }
}

fun ycLogD(msg: String? = "") {
    if (YcLogExt.mIsShowLogger) {
        XLog.d(msg.ycToNoEmpty())
    }
}

fun ycLogJson(json: String? = "") {
    if (YcLogExt.mIsShowLogger) {
        XLog.json(json)
    }
}
