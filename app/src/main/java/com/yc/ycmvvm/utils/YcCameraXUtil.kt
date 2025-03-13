package com.yc.ycmvvm.utils

import android.annotation.SuppressLint
import android.graphics.ImageFormat
import android.view.Surface
import androidx.camera.core.ImageProxy
import androidx.camera.core.internal.utils.ImageUtil

object YcCameraXUtil {


    @SuppressLint("RestrictedApi")
    fun toYv21(image: ImageProxy): ByteArray? {
        return when (image.format) {
            ImageFormat.YUV_420_888 -> {
                ImageUtil.yuv_420_888toNv21(image)
            }

            ImageFormat.JPEG -> {
                return null
            }

            else -> {
                null
            }
        }
    }

    /**
     *
     */
    @SuppressLint("RestrictedApi")
    fun toJpeg(image: ImageProxy): ByteArray? {
        val shouldCropImage = ImageUtil.shouldCropImage(image)
        val imageFormat = image.format

        return if (imageFormat == ImageFormat.JPEG) {
            if (!shouldCropImage) {
                ImageUtil.jpegImageToJpegByteArray(image)
            } else {
                ImageUtil.jpegImageToJpegByteArray(image, image.cropRect, 100)
            }
        } else if (imageFormat == ImageFormat.YUV_420_888) {
            ImageUtil.yuvImageToJpegByteArray(image, if (shouldCropImage) image.cropRect else null, 100, Surface.ROTATION_0)
        } else {
            null
        }
    }

}