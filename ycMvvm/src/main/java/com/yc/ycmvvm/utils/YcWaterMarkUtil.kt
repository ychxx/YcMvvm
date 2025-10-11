package com.yc.ycmvvm.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Typeface
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import com.yc.ycmvvm.config.YcInit
import com.yc.ycmvvm.file.YcFileUtils
import com.yc.ycmvvm.utils.YcResources.getColor
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Locale


object YcWaterMarkUtil {
    data class YcWaterMarkBean(
        var sourceFile: File,
        val content: String,
        var x: Int = 0,
        var y: Int = 0,
        val tvColor: Int = Color.parseColor("#E83E3E"),
        val saveFile: File = YcFileUtils.createFile(YcInit.mInstance.mDefaultSaveDirPath + "watermark_${sourceFile.name}")!!,
        var tvSize: Float? = null,
    )

    const val NEW_LINE_FLAG: String = "\n"

    /**
     * 添加文字水印
     */
    fun addTextWatermark(
        sourceFile: File,
        content: String,
        saveFile: File = YcFileUtils.createFile(YcInit.mInstance.mDefaultSaveDirPath + "watermark_${sourceFile.name}")!!
    ): File? {
        try {
            // 1. 解码图片（处理大图）
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(sourceFile.absolutePath, options)

            // 计算采样率
            options.inSampleSize = calculateInSampleSize(options, 2048, 2048)
            options.inJustDecodeBounds = false
            options.inPreferredConfig = Bitmap.Config.RGB_565 // 减少内存

            val original = BitmapFactory.decodeFile(sourceFile.absolutePath, options)

            // 2. 处理图片旋转
            val rotatedBitmap: Bitmap = rotateBitmapIfRequired(original, sourceFile)

            // 3. 创建带水印的Bitmap
            val watermarked = Bitmap.createBitmap(
                rotatedBitmap.width,
                rotatedBitmap.height,
                rotatedBitmap.config
            )

            val canvas = Canvas(watermarked)
            canvas.drawBitmap(rotatedBitmap, 0f, 0f, null)

            // 4. 绘制水印
            val paint = Paint()
            paint.color = Color.parseColor("#E83E3E")
            paint.textSize = canvas.height * 0.035f
            paint.setTypeface(Typeface.DEFAULT_BOLD)
            paint.isAntiAlias = true
            paint.setShadowLayer(0.5f, 0f, 0f, getColor("#F9000000"))
            val lineHeight = paint.descent() - paint.ascent()

            val startX = 0 + paint.textSize / 2
            var startY = 0 + paint.textSize
            for (line in content.split(NEW_LINE_FLAG)) {
                canvas.drawText(line, startX, startY, paint)
                startY += lineHeight
            }
            canvas.save()
            // 5. 保存图片
            saveBitmap(watermarked, saveFile)
            // 回收Bitmap
            original.recycle()
            rotatedBitmap.recycle()
            watermarked.recycle()
            return saveFile
        } catch (e: Exception) {
            Log.e("Watermark", "Error adding watermark: " + e.message)
            return null
        }
    }

    @Throws(IOException::class)
    private fun rotateBitmapIfRequired(bitmap: Bitmap, file: File): Bitmap {
        val exif = ExifInterface(file.absolutePath)
        val orientation: Int = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )

        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            else -> return bitmap
        }

        return Bitmap.createBitmap(
            bitmap, 0, 0,
            bitmap.width, bitmap.height, matrix, true
        )
    }

    @Throws(IOException::class)
    private fun saveBitmap(bitmap: Bitmap, outputFile: File) {
        FileOutputStream(outputFile).use { out ->
            if (outputFile.name.lowercase(Locale.getDefault()).endsWith(".png")) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            } else {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }
            out.flush()
        }
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while ((halfHeight / inSampleSize) >= reqHeight
                && (halfWidth / inSampleSize) >= reqWidth
            ) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}