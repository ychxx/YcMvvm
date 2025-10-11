package com.yc.ycmvvm.view.dialog

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.yc.ycmvvm.R
import com.yc.ycmvvm.databinding.YcDialogCommonBinding
import com.yc.ycmvvm.databinding.YcDialogLoadingBinding

/**
 * 通用的对话框
 */
class YcDialogCommon @JvmOverloads constructor(
    context: Context,
    mLifecycleOwner: LifecycleOwner,
    theme: Int = R.style.YcCommonDialogStyle,
    isCancelable: Boolean = false
) :
    Dialog(context, theme), YcIDialog<YcDialogCommon> {

    private var mOnLeftClick: YcOnClick? = null
    private var mOnRightClick: YcOnClick? = null

    val mViewBinding: YcDialogCommonBinding

    init {
        mLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                Log.e("YcCommonDialog", "onDestroy: ")
                if (this@YcDialogCommon.isShowing) {
                    dismiss()
                }
            }
        })
        mViewBinding = YcDialogCommonBinding.inflate(layoutInflater)
        setContentView(mViewBinding.root)

        //设置对话框位置大小
        val dialogWindow = window
        dialogWindow!!.setGravity(Gravity.CENTER)
        dialogWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        val lp = dialogWindow.attributes
        dialogWindow.attributes = lp //此处暂未设置偏移量
        setCancelable(isCancelable)
        setCanceledOnTouchOutside(isCancelable)
        mViewBinding.initView()
    }

    private fun YcDialogCommonBinding.initView() {
        dialogLeftBt.setOnClickListener {
            dismiss()
            if (mOnLeftClick != null) {
                mOnLeftClick?.onClick()
            }
        }
        dialogRightBt.setOnClickListener {
            dismiss()
            mOnRightClick?.onClick()
        }
    }

    fun setSingleBtnText(text: String?): YcDialogCommon {
        mViewBinding.dialogLeftBt.text = text ?: ""
        return this
    }

    /**
     * 设置右侧按钮点击事件
     */
    fun setSingleOnClick(onLeftClick: YcOnClick?): YcDialogCommon {
        mOnLeftClick = onLeftClick
        return this
    }

    fun showSingle() {
        mViewBinding.dialogRightBt.visibility = View.GONE
        mViewBinding.dialogLineV.visibility = View.GONE
        super.show()
    }

    /**
     * 设置提示语
     */
    override fun setMsg(msg: String?): YcDialogCommon {
        mViewBinding.dialogContentTv.text = msg ?: "暂无内容"
        return this
    }

    /**
     * 设置左侧按钮字符串
     */
    override fun setLeftBtnText(leftBtnText: String?): YcDialogCommon {
        mViewBinding.dialogLeftBt.text = leftBtnText ?: ""
        return this
    }

    /**
     * 设置左侧按钮点击事件
     */
    override fun setOnLeftClick(onLeftClick: YcOnClick): YcDialogCommon {
        mOnLeftClick = onLeftClick
        return this
    }

    /**
     * 设置右侧按钮字符串
     */
    override fun setRightBtnText(rightBtnText: String?): YcDialogCommon {
        mViewBinding.dialogRightBt.text = rightBtnText ?: ""
        mViewBinding.dialogRightBt.visibility = View.VISIBLE
        mViewBinding.dialogLineV.visibility = View.VISIBLE
        return this
    }

    /**
     * 设置右侧按钮点击事件
     */
    override fun setOnRightClick(onRightClick: YcOnClick): YcDialogCommon {
        mOnRightClick = onRightClick
        return this
    }

    override fun show() {
        mViewBinding.dialogRightBt.visibility = View.VISIBLE
        mViewBinding.dialogLineV.visibility = View.VISIBLE
        super.show()
    }
}