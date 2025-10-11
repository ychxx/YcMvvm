package com.yc.ycmvvm.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresPermission
import com.yc.ycmvvm.config.YcInit

/**
 * 震动
 */
object YcVibratorUtil {

    @RequiresPermission(android.Manifest.permission.VIBRATE)
    fun start(time: Long) {
        YcInit.mInstance.mApplication.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager?
                val vibrator = vibratorManager!!.defaultVibrator
                if (vibrator.hasVibrator()) {
                    vibrator.vibrate(VibrationEffect.createOneShot(time, VibrationEffect.DEFAULT_AMPLITUDE))
                }
            } else {
                // 对于 Android 10 及以下版本，使用旧方法
                val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
                vibrator?.vibrate(time) // 振动500毫秒
            }
        }
    }
}