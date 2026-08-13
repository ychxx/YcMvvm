package com.yc.ycmvvm.extension

import android.os.SystemClock
import android.widget.Chronometer
import kotlin.text.format

fun Chronometer.ycSetForm(prefix: String) {
    this.onChronometerTickListener = Chronometer.OnChronometerTickListener { chr ->
        val elapsedMs = SystemClock.elapsedRealtime() - chr.base
        val h = (elapsedMs / 3600000).toInt()
        val m = ((elapsedMs % 3600000) / 60000).toInt()
        val s = ((elapsedMs % 60000) / 1000).toInt()
        text = String.format("${prefix}%02d:%02d:%02d", h, m, s)
    }
}