package com.yc.ycmvvm.extension

import com.yc.ycmvvm.utils.YcTime.FORMAT_TIME
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * 获取当前月份的总天数
 */
fun Calendar.ycGetDaySum(): Int {
    return getActualMaximum(Calendar.DAY_OF_MONTH)
}

/**
 * 获取月份
 * 从0开始，0代表1月，11代表12月
 */
fun Calendar.ycGetMonth(): Int {
    return get(Calendar.MONTH)
}

/**
 * 获取年
 */
fun Calendar.ycGetYear(): Int {
    return get(Calendar.YEAR)
}

/**
 * 获取几号
 */
fun Calendar.ycGetDay(): Int {
    return get(Calendar.DAY_OF_MONTH)
}

/**
 * 获取两者相差天数
 */
fun Calendar.ycGetDayBetween(calendar: Calendar): Int {
    val startCopy = this.clone() as Calendar
    val endCopy = calendar.clone() as Calendar

    // 清除时间部分（小时、分钟、秒、毫秒）
    startCopy[Calendar.HOUR_OF_DAY] = 0
    startCopy[Calendar.MINUTE] = 0
    startCopy[Calendar.SECOND] = 0
    startCopy[Calendar.MILLISECOND] = 0

    endCopy[Calendar.HOUR_OF_DAY] = 0
    endCopy[Calendar.MINUTE] = 0
    endCopy[Calendar.SECOND] = 0
    endCopy[Calendar.MILLISECOND] = 0
    return ((endCopy.timeInMillis - startCopy.timeInMillis) / (24 * 60 * 60 * 1000)).toInt()
}

/**
 * 获取星期几对应的值
 * 周日(1)、 周一(2)、 周二(3)、 周三(4)、 周四(5)、 周五(6)、 周六(7)
 */
fun Calendar.ycGetWeek(): Int {
    return get(Calendar.DAY_OF_WEEK)
}

/**
 * 获取实际月份
 * 从1开始，1代表1月，12代表12月
 */
fun Calendar.ycGetMonthReal(): Int {
    return get(Calendar.MONTH) + 1
}

/**
 * 获取当前月份总天数
 */
fun Calendar.ycGetMonthSum(): Int {
    return getActualMaximum(Calendar.DAY_OF_MONTH)
}

val YC_WEEK = listOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")

/**
 * 获取星期几
 */
fun Calendar.ycGetWeekToString(weekList: List<String> = YC_WEEK): String {
    return YC_WEEK[get(Calendar.DAY_OF_WEEK) - 1]
}

/**
 * 转成字符串
 */
fun Calendar.ycToString(formatTime: String): String {
    return SimpleDateFormat(formatTime, Locale.getDefault()).format(time)
}

/**
 * 字符串转Calendar
 */
fun String.ycToCalendar(formatTime: String = FORMAT_TIME): Calendar {
    val sdf = SimpleDateFormat(formatTime, Locale.getDefault())
    val calendar = Calendar.getInstance()
    val timeDate: Date
    try {
        timeDate = sdf.parse(this)!!
        calendar.time = timeDate
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return calendar
}