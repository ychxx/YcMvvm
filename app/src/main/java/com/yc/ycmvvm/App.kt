package com.yc.ycmvvm

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.yc.ycmvvm.config.YcInit
import com.yc.ycmvvm.net.YcRetrofitUtil
import top.zibin.luban.LubanUtil

class App : Application() {
    companion object{
        lateinit var instance: App
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        YcInit.mInstance.initAll(this, hasLogShow = true, retrofitConfig = YcRetrofitUtil.YcRetrofitConfig("http://www.yc.com"))
        LubanUtil.init(this@App, true, null)
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            }

            override fun onActivityDestroyed(activity: Activity) {
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

        })
    }


}