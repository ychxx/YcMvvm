package com.yc.ycmvvm.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import com.yc.ycmvvm.config.YcInit

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
}