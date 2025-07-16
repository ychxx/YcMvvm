package com.yc.ycmvvm.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresPermission
import com.yc.ycmvvm.config.YcInit
import com.yc.ycmvvm.extension.ycIsEmpty

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
    @SuppressLint("MissingPermission")
    fun getDeviceUniqueId(context: Context = YcInit.mInstance.mApplication): String {
        val androidId = try {
            Settings.System.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            ""
        }
        return if (androidId.ycIsEmpty()) {
            YcEncryptionUtils.toMD5(androidId + Build.BRAND + Build.MODEL)
        } else {
            ""
        }
    }
}