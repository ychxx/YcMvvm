package com.yc.ycmvvm.data.db

import android.content.Context
import com.yc.ycmvvm.config.YcInit

object YcSharedPreferences {
    val ycSp by lazy { YcInit.mInstance.mApplication.getSharedPreferences("yc_db", Context.MODE_PRIVATE) }
    val YC_SP_KEY_DEVICE_ID = "yc_sp_key_device_id"
}