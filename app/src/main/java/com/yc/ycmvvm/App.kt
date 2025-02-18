package com.yc.ycmvvm

import android.app.Application
import com.yc.ycmvvm.config.YcInit

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        YcInit.mInstance.init(this, hasLogShow = true)
    }
}