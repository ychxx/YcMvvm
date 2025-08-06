package com.yc.ycmvvm.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresPermission
import androidx.core.content.edit
import com.yc.ycmvvm.config.YcInit
import com.yc.ycmvvm.data.db.YcSharedPreferences
import com.yc.ycmvvm.extension.ycIsEmpty
import com.yc.ycmvvm.extension.ycTry

object YcPhoneUtils {
    /**
     * 检测网络是否可用
     */
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    fun hasNetwork(): Boolean {
        val manager = YcInit.mInstance.mApplication.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = manager.activeNetworkInfo
        return null != info && info.isAvailable
    }

    /**
     * 获取设备唯一标识码
     * @param context
     * return String 签名不同则返回不同的值
     */
    @SuppressLint("MissingPermission", "HardwareIds")
    fun getDeviceUniqueId(context: Context = YcInit.mInstance.mApplication): String {
        var deviceUniqueId = ""
        val deviceIdDb = YcSharedPreferences.ycSp.getString(YcSharedPreferences.YC_SP_KEY_DEVICE_ID, "")
        if (deviceIdDb.isNullOrBlank()) {
            ycTry {
                val androidId = Settings.System.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
                if (!androidId.ycIsEmpty() && androidId != "9774d56d682e549c") {
                    deviceUniqueId = androidId
                }
            }
            val deviceIdNew = if (deviceUniqueId.ycIsEmpty()) {
                "9774d56d682e549c"
            } else {
                YcEncryptionUtils.toMD5(deviceUniqueId + Build.BRAND + Build.MODEL)
            }
            YcSharedPreferences.ycSp.edit(commit = true) {
                putString(YcSharedPreferences.YC_SP_KEY_DEVICE_ID, deviceIdNew)
            }
            return deviceIdNew
        } else {
            return deviceIdDb
        }
    }
}