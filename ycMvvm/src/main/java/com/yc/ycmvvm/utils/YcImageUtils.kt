package com.yc.ycmvvm.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import androidx.annotation.Nullable
import com.yc.ycmvvm.config.YcInit
import java.io.File
import java.io.IOException
import java.io.OutputStream


class YcImageUtils {
//    @Nullable
//    fun save2Album(
//        src: Bitmap,
//        dirName: String?,
//        format: CompressFormat,
//        quality: Int,
//        recycle: Boolean,
//        context:Context = YcInit.mInstance.mApplication
//    ): File? {
//        val safeDirName = if (TextUtils.isEmpty(dirName)) Utils.getApp().getPackageName() else dirName!!
//        val suffix = if (CompressFormat.JPEG == format) "JPG" else format.name
//        val fileName = System.currentTimeMillis().toString() + "_" + quality + "." + suffix
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
//            if (!UtilsBridge.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                Log.e("ImageUtils", "save to album need storage permission")
//                return null
//            }
//            val picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
//            val destFile = File(picDir, "$safeDirName/$fileName")
//            if (!save(src, destFile, format, quality, recycle)) {
//                return null
//            }
//            UtilsBridge.notifySystemToScan(destFile)
//            return destFile
//        } else {
//            val contentValues = ContentValues()
//            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
//            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/*")
//            val contentUri = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//            } else {
//                MediaStore.Images.Media.INTERNAL_CONTENT_URI
//            }
//            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_DCIM + "/" + safeDirName)
//            contentValues.put(MediaStore.MediaColumns.IS_PENDING, 1)
//            val uri: Uri = Utils.getApp().getContentResolver().insert(contentUri, contentValues) ?: return null
//            var os: OutputStream? = null
//            try {
//                os = Utils.getApp().getContentResolver().openOutputStream(uri)
//                src.compress(format, quality, os)
//
//                contentValues.clear()
//                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
//                Utils.getApp().getContentResolver().update(uri, contentValues, null, null)
//
//                return UtilsBridge.uri2File(uri)
//            } catch (e: Exception) {
//                Utils.getApp().getContentResolver().delete(uri, null, null)
//                e.printStackTrace()
//                return null
//            } finally {
//                try {
//                    os?.close()
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//            }
//        }
//    }
}