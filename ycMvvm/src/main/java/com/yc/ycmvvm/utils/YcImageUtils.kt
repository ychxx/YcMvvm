package com.yc.ycmvvm.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.yc.ycmvvm.R
import com.yc.ycmvvm.file.YcFileUtils
import java.io.File


object YcImageUtils {
    fun downloadImage(
        context: Context,
        url: String,
        onStart: (() -> Unit)? = null,
        onSuccess: ((resource: File) -> Unit)? = null,
        onFail: (() -> Unit)? = null,
        setRequest: ((request: Request?) -> Unit)? = null
    ) {
        Glide.with(context).downloadOnly().load(url)
            .into(object : Target<File> {
                override fun onStart() {
                }

                override fun onStop() {
                }

                override fun onDestroy() {
                }

                override fun onLoadStarted(placeholder: Drawable?) {
                    onStart?.invoke()
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    onFail?.invoke()
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun getSize(cb: SizeReadyCallback) {
                    cb.onSizeReady(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                }

                override fun removeCallback(cb: SizeReadyCallback) {
                }

                override fun setRequest(request: Request?) {
                    setRequest?.invoke(request)
                }

                override fun getRequest(): Request? {
                    return null
                }

                override fun onResourceReady(resource: File, transition: Transition<in File>?) {
                    onSuccess?.invoke(resource)
                }
            })
    }

    fun saveImageToGallery(
        context: Context,
        imageUrl: String,
        saveFileName: String = "IMG_${System.currentTimeMillis()}.jpg",
        onSuccess: ((savePath: String) -> Unit)? = null,
        onFail: ((error: String) -> Unit)? = null,
    ) {
        downloadImage(context, imageUrl, onSuccess = {
            val mimeType = getImageTypeWithMime(it.absolutePath)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+ 使用 MediaStore API
                saveImageApi29AndAbove(context, saveFileName, it, mimeType, onSuccess, onFail)
            } else {
                // Android 9 及以下版本使用传统方法
                saveImageBelowApi29(context, saveFileName, it, mimeType, onSuccess, onFail)
            }
        })

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveImageApi29AndAbove(
        context: Context,
        fileName: String,
        downloadFile: File,
        mimeType: String,
        onSuccess: ((savePath: String) -> Unit)? = null,
        onFail: ((error: String) -> Unit)?
    ) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, mimeType)
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }
        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {
            try {
                resolver.openOutputStream(uri)?.use { outputStream ->
                    downloadFile.inputStream().copyTo(outputStream)
                    // 更新IS_PENDING状态
                    contentValues.clear()
                    contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                    resolver.update(uri, contentValues, null, null)
                    onSuccess?.invoke(Environment.DIRECTORY_PICTURES)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                resolver.delete(uri, null, null)
                onFail?.invoke("保存失败: ${e.message}")
            }
        } ?: run {
            onFail?.invoke("保存失败: 无法创建文件")
        }
    }

    private fun saveImageBelowApi29(
        context: Context,
        fileName: String,
        downloadFile: File,
        mimeType: String,
        onSuccess: ((savePath: String) -> Unit)? = null,
        onFail: ((error: String) -> Unit)?
    ) {
        val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        YcFileUtils.createFile("$picturesDir/$fileName")?.let {
            try {
                downloadFile.copyTo(it, overwrite = true)
                downloadFile.delete()
                // 通知媒体库刷新
                MediaScannerConnection.scanFile(
                    context,
                    arrayOf(it.absolutePath),
                    arrayOf(mimeType),
                    null
                )
                onSuccess?.invoke(it.path)
            } catch (e: Exception) {
                e.printStackTrace()
                onFail?.invoke("保存失败: ${e.message}")
            }
        } ?: run {
            onFail?.invoke("保存失败: 无法创建文件")
            return
        }
    }

    fun getImageTypeWithMime(path: String?): String {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)
        var type = options.outMimeType
        type = if (TextUtils.isEmpty(type)) {
            "image/jpeg"
        } else {
            type.substring(6)
        }
        return type
    }
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