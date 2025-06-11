package com.yc.ycmvvm

import android.app.Application
import android.util.Log
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraXConfig
import com.hyperai.hyperlpr3.HyperLPR3
import com.hyperai.hyperlpr3.bean.HyperLPRParameter
import com.yc.ycmvvm.config.YcInit
import com.yc.ycmvvm.net.YcRetrofitUtil
import com.yc.ycmvvm.utils.YcCameraXProvider

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        YcInit.mInstance.initAll( this, hasLogShow = true, retrofitConfig = YcRetrofitUtil.YcRetrofitConfig("http://www.yc.com"))
        val param = HyperLPRParameter()
        param.setDetLevel(HyperLPR3.DETECT_LEVEL_LOW)
        param.setMaxNum(1)
        param.setRecConfidenceThreshold(0.85f)
        HyperLPR3.getInstance().init(this, param)
    }


}