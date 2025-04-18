package com.yc.ycmvvm.extension

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.yc.ycmvvm.data.entity.YcFileTempBean
import com.yc.ycmvvm.exception.YcException
import com.yc.ycmvvm.utils.permission.XXPermissionUtil
import com.yc.ycmvvm.utils.permission.YcPermissionUtil

class YcActivityResult {
    private var mPermissionUtil: YcPermissionUtil

    constructor(
        activity: FragmentActivity,
        call: (YcFileTempBean, flag: String) -> Unit,
        saveFile: YcFileTempBean = YcFileTempBean.create("temp", ".jpg")
    ) {
        mPermissionUtil = YcPermissionUtil(activity)
        mTakePictureUri = saveFile
        mLauncherTakePicture = activity.registerForActivityResult(ActivityResultContracts.TakePicture()) {
            call.invoke(mTakePictureUri!!, flag)
        }
        initData()
    }

    @SuppressLint("UseRequireInsteadOfGet")
    constructor(
        fragment: Fragment,
        call: (YcFileTempBean, flag: String) -> Unit,
        saveFile: YcFileTempBean = YcFileTempBean.create("temp", ".jpg")
    ) {
        mPermissionUtil = YcPermissionUtil(fragment)
        mTakePictureUri = saveFile
        mLauncherTakePicture = fragment.registerForActivityResult(ActivityResultContracts.TakePicture()) {
            call.invoke(mTakePictureUri!!, flag)
        }
        initData()
    }

    private fun initData() {
        mPermissionUtil.addPermission(XXPermissionUtil.CAMERA)
        mPermissionUtil.addPermission(*XXPermissionUtil.STORAGE)
    }

    var mLauncherTakePicture: ActivityResultLauncher<Uri>? = null
    private var mTakePictureUri: YcFileTempBean? = null
    var flag: String = "0"

    fun launcherTakePicture(flag: String = "0") {
        this.flag = flag
        mPermissionUtil.mSuccessCall = {
            if (mTakePictureUri == null || mLauncherTakePicture == null) {
                throw YcException("未注册", 0)
            }
            mLauncherTakePicture!!.launch(mTakePictureUri!!.fileUri)
        }
        mPermissionUtil.start()
    }

}
