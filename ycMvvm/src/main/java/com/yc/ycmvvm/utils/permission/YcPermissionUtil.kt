package com.yc.ycmvvm.utils.permission

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.hjq.permissions.permission.base.IPermission
import com.yc.ycmvvm.utils.YcActivityManager
import com.yc.ycmvvm.config.YcInit
import com.yc.ycmvvm.extension.ycLogE
import com.yc.ycmvvm.view.dialog.YcIDialog


/**
 * Creator: yc
 * Date: 2021/3/11 20:23
 * UseDes:权限申请帮助类
 */
open class YcPermissionUtil {


    private val context: Context
    private val lifecycleOwner: LifecycleOwner

    constructor(context: Context, lifecycleOwner: LifecycleOwner) {
        this.context = context
        this.lifecycleOwner = lifecycleOwner
        initDialog()
    }

    constructor(activity: FragmentActivity) {
        this.context = activity
        this.lifecycleOwner = activity
        initDialog()
    }

    @SuppressLint("UseRequireInsteadOfGet")
    constructor(fragment: Fragment) {
        this.context = fragment.context!!
        this.lifecycleOwner = fragment
        initDialog()
    }

    private var mXXRequestPermission: XXRequestPermission = XXRequestPermission()
    fun addPermission(vararg permissions: IPermission) {
        permissions.forEach {
            mXXRequestPermission.permission.add(it)
        }
    }

    open var mDialog: YcIDialog<*>? = null

    private fun initDialog() {
        mDialog = YcInit.mInstance.mDefaultPermissionDialog.invoke(this.context, this.lifecycleOwner)
    }

    /**
     * 权限全部同意回调
     */
    var mSuccessCall: (() -> Unit)? = null

    /**
     * 用户点击“拒绝"回调
     */
    var mFailCall: ((List<IPermission>) -> Unit) = {
        mDialog?.apply {
            setMsg("软件运行必要权限申请被拒绝!")
            setLeftBtnText("退出")
            setOnLeftClick {
                YcActivityManager.finishCurrentActivity()
            }
            setRightBtnText("重试")
            setOnRightClick {
                start()
            }
            show()
        }
    }

    /**
     * 用户点击“不再询问"回调
     */
    var mNeverAskAgainCall: (() -> Unit) = {
        val current = YcActivityManager.getCurrentActivity()
        if (current == null) {
            mDialog?.apply {
                setMsg("权限申请被禁止，请手动开启!")
                setLeftBtnText("退出")
                setOnLeftClick {
                    YcActivityManager.finishCurrentActivity()
                }
                setRightBtnText("重试")
                setOnRightClick {
                    start()
                }
                show()
            }
        } else {
            mDialog?.apply {
                setMsg("权限申请被禁止，请手动开启!")
                setLeftBtnText("前往设置")
                setOnLeftClick {
                    XXPermissions.startPermissionActivity(current)
                }
                setRightBtnText("重试")
                setOnRightClick {
                    start()
                }
                show()
            }
        }
    }

    /**
     * 申请开始回调
     */
    var mStartCall: (() -> Unit)? = null

    /**
     * 开始申请权限
     * @receiver AppCompatActivity  在AppCompatActivity里调用
     * 注：在onCreate之后的生命周期里调用
     */
    open fun start() {
        mStartCall?.invoke()
        mXXRequestPermission.hasSuccess = mXXRequestPermission.permission.isEmpty() ||
                XXPermissions.isGrantedPermissions(context, mXXRequestPermission.permission)
        if (!mXXRequestPermission.hasSuccess) {
            ycLogE("申请正常权限" + mXXRequestPermission.permission.toString())
            XXPermissions.with(context)
                .permissions(mXXRequestPermission.permission)
                .request { grantedList, deniedList ->
                    val allGranted = deniedList.isEmpty()
                    if (allGranted) {
                        mSuccessCall?.invoke()
                    } else {
                        val current = YcActivityManager.getCurrentActivity()
                        val doNotAskAgain = if (current != null) {
                            XXPermissions.isDoNotAskAgainPermissions(current, deniedList)
                        } else {
                            false
                        }
                        if (doNotAskAgain) {
                            mNeverAskAgainCall.invoke()
                        } else {
                            mFailCall.invoke(deniedList)
                        }
                    }
                }
        } else {
            mSuccessCall?.invoke()
        }
    }

    private data class XXRequestPermission(
        val permission: ArrayList<IPermission> = arrayListOf(),
        var hasSuccess: Boolean = false,
    )
}