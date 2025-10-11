package com.yc.ycmvvm.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.yc.ycmvvm.view.dialog.YcDialogCommon
import java.io.File

/**
 * app版本相关信息
 */
class YcInstallUtil(val activity: FragmentActivity) {
    private var mInstallRegister: ActivityResultLauncher<Intent>? = null
    private val mFragmentDialog: YcDialogCommon by lazy { YcDialogCommon(activity, activity) }

    init {
        activity.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                super.onCreate(owner)
                register()
            }
        })
    }

    var mApkFile: File? = null

    /**
     * 用户点击“拒绝"回调
     */
    var mFailCall: ((errorMsg: String) -> Unit) = {
        mFragmentDialog.apply {
            setMsg(it)
            setLeftBtnText("退出")
            setOnLeftClick {
                activity.finish()
            }
            setRightBtnText("重试")
            setOnRightClick {
                installApk()
            }
        }.show()
    }

    /**
     * 注册
     */
    fun register() {
        if (mInstallRegister == null) {
            mInstallRegister = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode != Activity.RESULT_OK) { //安装apk失败
                    mFailCall.invoke("安装apk失败")
                }
            }
        }
    }

    /**
     * 开始申请权限
     * 注：在onCreate生命周期里调用
     */
    fun startOnCreate() {
        register()
        installApk()
    }

    /**
     * 安装apk
     */
    fun installApk() {
        if (mInstallRegister != null && mApkFile != null) {
            val intent = Intent(Intent.ACTION_VIEW)
            val data: Uri
            // 判断版本大于等于7.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                data = FileProvider.getUriForFile(activity, "${YcVersionUtil.getPackageName(activity)}.fileprovider", mApkFile!!)
                // 给目标应用一个临时授权
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    .addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            } else {
                data = Uri.fromFile(mApkFile)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            intent.setDataAndType(data, "application/vnd.android.package-archive")
            mInstallRegister!!.launch(intent)
        } else {
            Log.e("YcInstallUtil", "安装失败：mApkFile文件为空!!!!")
//            throw Throwable("安装失败：mApkFile文件为空!!!!")
            mFailCall.invoke("安装apk失败，apk文件不存在")
        }
    }
}