package com.yc.ycmvvm.utils

import java.math.BigDecimal
import java.math.RoundingMode

fun Number.ycToBigDecimal(): BigDecimal = when (this) {
    is BigDecimal -> this  // 直接处理 BigDecimal 类型
    is Int, is Long, is Short, is Byte -> BigDecimal.valueOf(toLong())
    is Double, is Float -> BigDecimal.valueOf(toDouble())
    else -> throw IllegalArgumentException("Number转换BigDecimal异常: ${this.javaClass}")
}

fun BigDecimal.ycHasZero(): Boolean = this.compareTo(BigDecimal.ZERO) == 0

object YcBigDecimalUtils {

    /**
     * 安全除法（避免除零异常）
     * @param scale 小数位数（默认2位）
     * @param roundingMode 舍入模式（默认四舍五入）
     * @return 计算结果，除数为零时返回 BigDecimal.ZERO
     */
    fun divide(a: Number, b: Number, scale: Int = 2, roundingMode: RoundingMode = RoundingMode.HALF_UP): BigDecimal =
        divideSafe(a.ycToBigDecimal(), b.ycToBigDecimal(), scale, roundingMode)

    fun divideSafe(
        dividend: BigDecimal,
        divisor: BigDecimal,
        scale: Int = 2,
        roundingMode: RoundingMode = RoundingMode.HALF_UP
    ): BigDecimal = when {
        divisor.ycHasZero() -> BigDecimal.ZERO
        else -> dividend.divide(divisor, scale, roundingMode)
    }

    /**
     * 相乘
     */
    fun multiply(a: Number, b: Number): BigDecimal = a.ycToBigDecimal().multiply(b.ycToBigDecimal())


    /**
     * 加法
     */
    fun add(vararg numbers: Number): BigDecimal =
        numbers.fold(BigDecimal.ZERO) { acc, num ->
            acc.add(num.ycToBigDecimal())
        }

    /**
     * 相减
     */
    fun subtract(a: Number, b: Number): BigDecimal = a.ycToBigDecimal().subtract(b.ycToBigDecimal())

    /**
     * 大于
     */
    fun greater(a: BigDecimal, b: BigDecimal): Boolean = a.compareTo(b) == 1

    /**
     * 大于等于
     */
    fun greaterOrEqual(a: BigDecimal, b: BigDecimal): Boolean = a.compareTo(b).let {
        it == 1 || it == 0
    }

    /**
     * 小于
     */
    fun less(a: BigDecimal, b: BigDecimal): Boolean = a.compareTo(b) == -1

    /**
     * 小于等于
     */
    fun lessOrEqual(a: BigDecimal, b: BigDecimal): Boolean = a.compareTo(b).let {
        it == -1 || it == 0
    }
}