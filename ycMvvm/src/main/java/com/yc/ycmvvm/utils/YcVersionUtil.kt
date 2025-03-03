
package com.yc.ycmvvm.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import com.yc.ycmvvm.config.YcInit
import java.io.File

/**
 * app版本相关信息
 */
object YcVersionUtil {
    /**
     *
     * @param activity FragmentActivity
     * @param apkFile File
     * @param installFail Function0<Unit>
     */
    @SuppressLint("WrongConstant")
    @JvmStatic
    fun installApk(activity: FragmentActivity, apkFile: File, mRegister: ActivityResultLauncher<Intent>, installFail: (() -> Unit)? = null) {
        val intent = Intent(Intent.ACTION_VIEW)
        val data: Uri
        // 判断版本大于等于7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            data = FileProvider.getUriForFile(activity, "$packageName.fileprovider", apkFile)
            // 给目标应用一个临时授权
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                .addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        } else {
            data = Uri.fromFile(apkFile)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive")
        /*activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode != Activity.RESULT_OK) { //安装apk失败
                if (installFail != null) {
                    installFail()
                }
            }
        }.launch(intent)*/

        mRegister.launch(intent)
    }

    /**
     * 获取版本号
     */
    @JvmStatic
    val versionCode: Long = getVersionCode(YcInit.mInstance.mApplication)

    /**
     * 获取版本号
     */
    @JvmStatic
    fun getVersionCode(context: Context): Long {
        try {
            val packageInfo: PackageInfo? = context.packageManager.getPackageInfo(context.packageName, 0)
            if (packageInfo != null) {
                return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                    packageInfo.versionCode.toLong()//添加这个原因：在夜神模拟器上会闪退 (适配28以下)
                } else {
                    packageInfo.longVersionCode
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return -1
    }

    /**
     * 获取版本名
     */
    @JvmStatic
    val versionName = getVersionName(YcInit.mInstance.mApplication)

    /**
     * 获取版本名
     */
    @JvmStatic
    fun getVersionName(context: Context): String {
        try {
            val packageInfo: PackageInfo? = context.packageManager.getPackageInfo(context.packageName, 0)
            if (packageInfo != null) {
                return packageInfo.versionName
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 获取包名
     */
    @JvmStatic
    val packageName = getPackageName(YcInit.mInstance.mApplication)

    /**
     * 获取包名
     */
    @JvmStatic
    fun getPackageName(context: Context): String {
        return context.packageName
    }

    /**
     * 获取状态栏高度
     */
    @JvmStatic
    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
}