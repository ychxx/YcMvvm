package com.yc.ycmvvm.data.entity

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.yc.ycmvvm.config.YcInit
import java.io.File

data class YcFileTempBean(
    val file: File,
    val fileUri: Uri
) {
    companion object {
        fun create(
            fileNamePrefix: String,
            fileNameSuffix: String,
            context: Context = YcInit.mInstance.mApplication,
            directory: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            authority: String = "${context.packageName}.fileprovider",
        ): YcFileTempBean {
            val file = File.createTempFile(
                fileNamePrefix,
                fileNameSuffix,
                directory
            )
            return YcFileTempBean(file, FileProvider.getUriForFile(context, authority, file))
        }
    }
}