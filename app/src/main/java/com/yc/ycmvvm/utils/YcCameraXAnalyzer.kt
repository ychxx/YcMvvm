package com.yc.ycmvvm.utils

import android.graphics.ImageFormat
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.yc.ycmvvm.extension.toByteArray

class YcCameraXAnalyzer : ImageAnalysis.Analyzer {
    private val supportedImageFormats = listOf(
        ImageFormat.YUV_420_888,
        ImageFormat.YUV_422_888,
        ImageFormat.YUV_444_888
    )

    override fun analyze(image: ImageProxy) {
        //检查扫描图像代理的格式是否在支持的格式当中

        if (image.format in supportedImageFormats) {
            //获取原始数据
            val bytes = image.planes.first().buffer.toByteArray()


            try {

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                image.close()
            }
        }
    }
}