package com.yc.ycmvvm.view.platenum

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import com.yc.ycmvvm.R
import com.yc.ycmvvm.data.constans.YcPlateNumData
import com.yc.ycmvvm.data.constans.YcPlateNumData.getYcPlateNumFrameBg
import com.yc.ycmvvm.databinding.YcPlateNumFrameBinding
import com.yc.ycmvvm.extension.ycIsEmpty
import com.yc.ycmvvm.extension.ycIsNotEmpty
import com.yc.ycmvvm.extension.ycSetTextColorRes
import com.yc.ycmvvm.extension.ycVisibility

/**
 * 输入框容器
 */
open class YcPlateNumFrameView : LinearLayout {
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context, attrs)
    }

    protected var mPlateNumBtnList: MutableList<Button> = arrayListOf()
    private lateinit var mViewBinding: YcPlateNumFrameBinding

    /**
     * 车牌颜色
     */
    @YcPlateNumData.YcPlateNumColorType
    private var mPlateNumColorTypeSelect: String = YcPlateNumData.PLATE_NUM_TYPE_BLUE

    private var mPlateNumColorTypeAll: List<@YcPlateNumData.YcPlateNumColorType String> = listOf()
    private fun initView(context: Context, attrs: AttributeSet?) {
        mViewBinding = YcPlateNumFrameBinding.inflate(LayoutInflater.from(context), this, false)
        mPlateNumBtnList.add(mViewBinding.plateNum0)
        mPlateNumBtnList.add(mViewBinding.plateNum1)
        mPlateNumBtnList.add(mViewBinding.plateNum2)
        mPlateNumBtnList.add(mViewBinding.plateNum3)
        mPlateNumBtnList.add(mViewBinding.plateNum4)
        mPlateNumBtnList.add(mViewBinding.plateNum5)
        mPlateNumBtnList.add(mViewBinding.plateNum6)
        mPlateNumBtnList.add(mViewBinding.energyBtn)
        mPlateNumKeyboardPopup = YcPlateNumKeyboardPopup(context).apply {
            mClick = { context, type ->
                when (type) {
                    YcPlateNumData.PLATE_NUM_KEYBOARD_TYPE_DEL -> {
                        if (mSelectBtnIndex > 0) {
                            if (mPlateNumBtnList[mSelectBtnIndex].text.toString().ycIsEmpty()) {
                                //当前为空，前一位也设为空（小程序是这样的）
                                mPlateNumBtnList[mSelectBtnIndex - 1].text = ""
                            }
                            mPlateNumBtnList[mSelectBtnIndex].text = ""
                            mSelectBtnIndex--
                            updatePlateNumBtn()
                        } else {
                            mPlateNumBtnList[mSelectBtnIndex].text = ""
                            updatePlateNumBtn()
                        }
                    }

                    YcPlateNumData.PLATE_NUM_KEYBOARD_TYPE_OK -> {

                    }

                    YcPlateNumData.PLATE_NUM_KEYBOARD_TYPE_DATA -> {
                        mPlateNumBtnList[mSelectBtnIndex].text = context
                        if (mSelectBtnIndex < mPlateNumBtnList.size - 1) {
                            mSelectBtnIndex++
                        }
                        updatePlateNumBtn()
                    }
                }

            }
        }
        addView(mViewBinding.root)
    }

    private var mSelectBtnIndex = -1
    private fun initPlateNumBtn() {
        mPlateNumBtnList.forEachIndexed { index, button ->
            button.setOnClickListener {
                mSelectBtnIndex = index
                keyboardPopupShow()
            }
        }
        updatePlateNumBtn()
    }

    private fun updatePlateNumBtn() {
        var plateNum = ""
        mPlateNumBtnList.forEach {
            plateNum += it.text.toString()
        }
        mViewBinding.energyMaskTv.ycVisibility(mViewBinding.energyBtn.text.toString().ycIsEmpty())
        if (plateNum.length >= 7) {
            mPlateNumColorTypeAll = YcPlateNumData.getYcPlateNumColorType(plateNum)
            mPlateNumColorTypeSelect = mPlateNumColorTypeAll[0]
        } else {
            mPlateNumColorTypeAll = listOf()
            mPlateNumColorTypeSelect = YcPlateNumData.PLATE_NUM_TYPE_NONE
        }
        mPlateNumBtnList.forEachIndexed { index, button ->
            button.setBackgroundResource(getYcPlateNumFrameBg(index, mPlateNumColorTypeSelect, index == mSelectBtnIndex, button.text.toString().ycIsEmpty()))
            if (mPlateNumColorTypeSelect == YcPlateNumData.PLATE_NUM_TYPE_BLACK) {
                button.ycSetTextColorRes(R.color.yc_plate_num_text_black)
            } else {
                button.ycSetTextColorRes(R.color.yc_plate_num_text_other)
            }
        }
        mPlateNumKeyboardPopup.updateKeyboardData(mSelectBtnIndex)
    }

    private lateinit var mPlateNumKeyboardPopup: YcPlateNumKeyboardPopup

    var mKeyboardShowAsDropDown: View = this

    /**
     * 显示车牌号虚拟键盘
     */
    fun keyboardPopupShow() {
        mPlateNumKeyboardPopup.updateKeyboardData(mSelectBtnIndex)
        mPlateNumKeyboardPopup.keyboardPopupShow(mKeyboardShowAsDropDown)
    }

    fun keyboardPopupHide() {
        mPlateNumKeyboardPopup.keyboardPopupHide()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initPlateNumBtn()
    }
}

