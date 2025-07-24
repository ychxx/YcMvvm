package com.yc.ycmvvm.utils

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaDrm
import android.net.ConnectivityManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat.getSystemService
import com.yc.ycmvvm.config.YcInit
import com.yc.ycmvvm.extension.ycIsEmpty
import com.yc.ycmvvm.extension.ycTry
import java.io.Serial

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
        ycTry {
            val androidId = Settings.System.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            if (!androidId.ycIsEmpty() && androidId != "9774d56d682e549c") {
                deviceUniqueId = androidId
            }
        }
        return if (deviceUniqueId.ycIsEmpty()) {
            "9774d56d682e549c"
        } else {
            YcEncryptionUtils.toMD5(deviceUniqueId + Build.BRAND + Build.MODEL)
        }
    }
}