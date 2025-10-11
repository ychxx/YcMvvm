package com.yc.ycmvvm.extension

import android.view.View
import com.yc.ycmvvm.utils.YcTime
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.math.RoundingMode
import java.nio.ByteBuffer
import java.text.DecimalFormat
import java.util.*

/**
 * 用于类型转换
 */
object YcAnyExt {
    var mCommonDefaultString: String = "-"
    var mCommonDefaultDouble: Double = 0.0
    var mCommonDefaultInt: Int = 0
    var mCommonDefaultFloat: Float = 0f

    /**
     * 小数保留的位数格式
     */
    var mCommonDoubleFormat: String = "0.##"
}

/**
 * 将许空字符串转为非空字符串（空用ycGetDefault()里的值替代）
 */
fun String?.ycToNoEmpty(defaultData: String = YcAnyExt.mCommonDefaultString): String {
    return if (this == null || this.isEmpty()) {
        defaultData
    } else {
        this
    }
}

/**
 * 获取非空值，空则用默认值，非空时后面加上单位
 * @param unit String   单位
 */
fun String?.ycToNoEmptyHasUnit(unit: String, defaultData: String = YcAnyExt.mCommonDefaultString): String {
    return if (this == null || this.isEmpty()) {
        defaultData
    } else {
        this + unit
    }
}

fun String?.ycToInt(): Int? = ycTryReturnData(block = {
    return@ycTryReturnData this?.toIntOrNull()
}, error = {
    return@ycTryReturnData null
})
fun String?.ycToLong(): Long? = ycTryReturnData(block = {
    return@ycTryReturnData this?.toLongOrNull()
}, error = {
    return@ycTryReturnData null
})

/**
 * 转成Double类型，格式错误返回空
 */
fun String?.ycToDouble(): Double? = ycTryReturnData(block = {
    return@ycTryReturnData this?.toDouble()
}, error = {
    return@ycTryReturnData null
})

/**
 * 转成非空的Double类型，格式错误返回空
 */
fun String?.ycToDoubleNoEmpty(defaultData: Double = YcAnyExt.mCommonDefaultDouble): Double = ycTryReturnData(block = {
    return@ycTryReturnData this?.toDouble() ?: defaultData
}, error = {
    return@ycTryReturnData defaultData
})

/**
 * 先转成Double类型，执行block()，再格式化，最后转为String
 */
fun String?.ycToDoubleAndFormatToString(block: (Double) -> Double, format: String = YcAnyExt.mCommonDoubleFormat): String? = ycTryReturnData(block = {
    if (this == null) {
        return@ycTryReturnData null
    } else {
        return block(this.toDouble()).ycFormatAndToString(format)
    }
}, error = {
    return@ycTryReturnData null
})

/**
 * 转成Float类型，格式错误返回空
 */
fun String?.ycToFloat(): Float? = ycTryReturnData(block = {
    return@ycTryReturnData this?.toFloat()
}, error = {
    return@ycTryReturnData null
})

/**
 * 转成非空的Float类型，格式错误返回空
 */
fun String?.ycToFloatNoEmpty(defaultData: Float = YcAnyExt.mCommonDefaultFloat): Float = ycTryReturnData(block = {
    return@ycTryReturnData this?.toFloat() ?: defaultData
}, error = {
    return@ycTryReturnData defaultData
})

/**
 * 先转成Float类型，执行block()，再格式化，最后转为String
 */
fun String?.ycToFloatAndFormatToString(block: (Float) -> Float, format: String = YcAnyExt.mCommonDoubleFormat): String? = ycTryReturnData(block = {
    if (this == null) {
        return@ycTryReturnData null
    } else {
        return block(this.toFloat()).ycFormatAndToString(format)
    }
}, error = {
    return@ycTryReturnData null
})

/**
 * 生成RequestBody
 */
fun String.ycToRequestBody(mediaType: MediaType? = "application/json".toMediaTypeOrNull()): RequestBody {
    return this.toRequestBody(mediaType)
}

/**
 * String的时间格式转换
 */
fun String?.ycToTime(
    defaultData: String = YcAnyExt.mCommonDefaultString,
    formatTimeInput: String = YcTime.FORMAT_TIME,
    formatTimeOut: String = YcTime.FORMAT_TIME_MONTH_DAY
): String {
    return if (this != null) {
        YcTime.stringToString(this, formatTimeInput, formatTimeOut)
    } else {
        defaultData
    }
}

/**
 * 字符串判断是否为空
 */
fun String?.ycIsNotEmpty(): Boolean {
    return this != null && this.isNotEmpty()
}

/**
 *
 * 字符串判断是否为非空
 */
fun String?.ycIsEmpty(): Boolean {
    return this == null || this.isEmpty()
}

/**
 * 将许空Int转为非空Int（空用0替代）
 */
fun Int?.ycToNoEmpty(defaultData: Int = YcAnyExt.mCommonDefaultInt): Int {
    return this ?: defaultData
}

/**
 * 将许空Int转为非空字符串（空用mCommonDefault替代）
 */
fun Int?.ycToStringNoEmpty(defaultData: String = YcAnyExt.mCommonDefaultString): String {
    return this?.toString() ?: defaultData
}

/**
 * 将许空Int转为非空,非0字符串（空和0都用"-"替代）
 */
fun Int?.ycToStringNoEmptyNoZero(defaultData: String = YcAnyExt.mCommonDefaultString): String {
    return if (this == null || this == 0) {
        return defaultData
    } else {
        "$this"
    }
}

/**
 * 将许空Float转为非空Float（空用0替代）
 */
fun Float?.ycToNoEmpty(defaultData: Float = YcAnyExt.mCommonDefaultFloat): Float {
    return this ?: defaultData
}

/**
 * 将许空Float转为非空字符串（空用mCommonDefault替代）
 */
fun Float?.ycToStringNoEmpty(defaultData: String = YcAnyExt.mCommonDefaultString): String {
    return this?.toString() ?: defaultData
}

/**
 * 保留2位小数，并省略无效的0
 * @receiver Double?
 * @return String?
 */
fun Float?.ycFormat(format: String = YcAnyExt.mCommonDoubleFormat): Float? = ycFormatAndToString(format)?.toFloat()

/**
 * 保留2位小数，并省略无效的0
 * @receiver Double?
 * @return String?
 */
fun Float?.ycFormatAndToString(format: String = YcAnyExt.mCommonDoubleFormat): String? = ycTryReturnData(block = {
    return if (this == null) {
        null
    } else {
        DecimalFormat(format).apply {
            //未保留小数的舍弃规则，RoundingMode.FLOOR表示直接舍弃。
            //未保留小数的舍弃规则，RoundingMode.HALF_UP表示四舍五入
            roundingMode = RoundingMode.HALF_UP
        }.format(this)
    }
}, error = {
    return@ycTryReturnData null
})

/**
 * 保留2位小数，并省略无效的0
 * @receiver Double?
 * @return String?
 */
fun Double?.ycFormat(format: String = YcAnyExt.mCommonDoubleFormat): Double? = ycFormatAndToString(format)?.toDouble()

/**
 * 保留2位小数，并省略无效的0
 * @receiver Double?
 * @return String?
 */
fun Double?.ycFormatAndToString(format: String = YcAnyExt.mCommonDoubleFormat): String? = ycTryReturnData(block = {
    return if (this == null) {
        null
    } else {
        DecimalFormat(format).apply {
            //未保留小数的舍弃规则，RoundingMode.FLOOR表示直接舍弃。
            //未保留小数的舍弃规则，RoundingMode.HALF_UP表示四舍五入
            roundingMode = RoundingMode.HALF_UP
        }.format(this)
    }
}, error = {
    return@ycTryReturnData null
})

/**
 * 非空且为true
 */
fun Boolean?.ycIsTrue(): Boolean {
    return this != null && this
}

/**
 *  非空且为是false
 */
fun Boolean?.ycIsFalse(): Boolean {
    return this != null && !this
}

/**
 *  当true时为可见
 */
fun Boolean?.ycToVisibleOrInvisibleWhenTrue(): Int {
    return if (this == true) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}

/**
 *  当false时为可见
 */
fun Boolean?.ycToVisibleOrInvisibleWhenFalse(): Int {
    return if (this == false) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}

/**
 *  当true时为可见
 */
fun Boolean?.ycToVisibleOrGoneWhenTrue(): Int {
    return if (this == true) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

/**
 *  当false时为可见
 */
fun Boolean?.ycToVisibleOrGoneWhenFalse(): Int {
    return if (this == false) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

fun ByteBuffer.toByteArray(): ByteArray {
    rewind()
    return ByteArray(remaining()).also {
        get(it)
    }
}

/**
 * 集合判断是否为空集合
 */
fun <T> List<T>?.ycIsNotEmpty(): Boolean {
    return !this.isNullOrEmpty()
}

/**
 * 集合判断是否为非空集合
 */
fun <T> List<T>?.ycIsEmpty(): Boolean {
    return this.isNullOrEmpty()
}

/**
 * 获取指定索引的元素
 */
fun <T> List<T>?.ycGet(index: Int): T? {
    return if (this.ycIsEmpty() || this!!.size <= index) {
        null
    } else {
        this[index]
    }
}
