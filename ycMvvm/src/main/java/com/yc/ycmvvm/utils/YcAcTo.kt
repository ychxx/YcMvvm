package com.yc.ycmvvm.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import com.yc.ycmvvm.config.YcInit

object YcAcTo {
    /**
     * 跳转到系统设置界面
     */
    fun toSysSetting(activityResultLauncher: ActivityResultLauncher<Intent>) {
        val intent = Intent(Settings.ACTION_SETTINGS)
        activityResultLauncher.launch(intent)
    }

    /**
     * 跳转到APP设置界面
     */
    fun toAppSetting(activityResultLauncher: ActivityResultLauncher<Intent>, packageName: String = YcVersionUtil.packageName) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", packageName, null)
        if (areActivityIntent(YcInit.mInstance.mApplication, intent)) {
            activityResultLauncher.launch(intent)
        } else {
            val intentAppAll = Intent(Settings.ACTION_APPLICATION_SETTINGS)
            if (areActivityIntent(YcInit.mInstance.mApplication, intentAppAll)) {
                activityResultLauncher.launch(intentAppAll)
            } else {
                val intentManageApp = Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS)
                activityResultLauncher.launch(intentManageApp)
            }
        }
    }

    /**
     * 判断这个意图的 Activity 是否存在
     */
    fun areActivityIntent(context: Context, intent: Intent): Boolean {
        // 这里为什么不用 Intent.resolveActivity(intent) != null 来判断呢？
        // 这是因为在 OPPO R7 Plus （Android 5.0）会出现误判，明明没有这个 Activity，却返回了 ComponentName 对象
        val packageManager = context.packageManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return packageManager.queryIntentActivities(
                intent,
                PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_DEFAULT_ONLY.toLong())
            ).isNotEmpty()
        }
        return packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).isNotEmpty()
    }
}