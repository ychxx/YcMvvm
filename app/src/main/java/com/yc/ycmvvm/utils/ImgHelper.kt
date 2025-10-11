package com.yc.ycmvvm.utils

import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.os.Environment
import android.util.Log
import androidx.camera.core.ImageProxy
import java.io.File
import java.io.FileOutputStream

object ImgHelper {
    var TAG: String = "rfDevImg"

    // 获取到YuvImage对象 然后存文件
    fun useYuvImgSaveFile(imageProxy: ImageProxy, outputYOnly: Boolean) {
        val wid = imageProxy.width
        val height = imageProxy.height
        Log.d(TAG, "宽高: $wid, $height")

        val yuvImage = toYuvImage(imageProxy)
        val file = File(Environment.getExternalStorageDirectory(), "z_" + System.currentTimeMillis() + ".png")
        saveYuvToFile(file, wid, height, yuvImage)
        Log.d(TAG, "rustfisher.com 存储了$file")

        if (outputYOnly) { // 仅仅作为功能演示
            val yImg = toYOnlyYuvImage(imageProxy)
            val yFile = File(Environment.getExternalStorageDirectory(), "y_" + System.currentTimeMillis() + ".png")
            saveYuvToFile(yFile, wid, height, yImg)
            Log.d(TAG, "rustfisher.com 存储了$yFile")
        }
    }

    // 仅作为示例使用
    fun toYOnlyYuvImage(imageProxy: ImageProxy): YuvImage {
        require(imageProxy.format == ImageFormat.YUV_420_888) { "Invalid image format" }
        val width = imageProxy.width
        val height = imageProxy.height
        val yBuffer = imageProxy.planes[0].buffer
        val numPixels = (width * height * 1.5f).toInt()
        val nv21 = ByteArray(numPixels)
        var index = 0
        val yRowStride = imageProxy.planes[0].rowStride
        val yPixelStride = imageProxy.planes[0].pixelStride
        for (y in 0 until height) {
            for (x in 0 until width) {
                nv21[index++] = yBuffer[y * yRowStride + x * yPixelStride]
            }
        }
        return YuvImage(nv21, ImageFormat.NV21, width, height, null)
    }

    fun toYuvImage(image: ImageProxy): YuvImage {
        require(image.format == ImageFormat.YUV_420_888) { "Invalid image format" }
        val width = image.width
        val height = image.height

        // 拿到YUV数据
        val yBuffer = image.planes[0].buffer
        val uBuffer = image.planes[1].buffer
        val vBuffer = image.planes[2].buffer

        val numPixels = (width * height * 1.5f).toInt()
        val nv21 = ByteArray(numPixels) // 转换后的数据
        var index = 0

        // 复制Y的数据
        val yRowStride = image.planes[0].rowStride
        val yPixelStride = image.planes[0].pixelStride
        for (y in 0 until height) {
            for (x in 0 until width) {
                nv21[index++] = yBuffer[y * yRowStride + x * yPixelStride]
            }
        }

        // 复制U/V数据
        val uvRowStride = image.planes[1].rowStride
        val uvPixelStride = image.planes[1].pixelStride
        val uvWidth = width / 2
        val uvHeight = height / 2

        for (y in 0 until uvHeight) {
            for (x in 0 until uvWidth) {
                val bufferIndex = (y * uvRowStride) + (x * uvPixelStride)
                nv21[index++] = vBuffer[bufferIndex]
                nv21[index++] = uBuffer[bufferIndex]
            }
        }
        return YuvImage(nv21, ImageFormat.NV21, width, height, null)
    }

    fun saveYuvToFile(file: File, wid: Int, height: Int, yuvImage: YuvImage) {
        try {
            val c = file.createNewFile()
            Log.d(TAG, "$file created: $c")
            val fos = FileOutputStream(file)
            yuvImage.compressToJpeg(Rect(0, 0, wid, height), 100, fos)
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
