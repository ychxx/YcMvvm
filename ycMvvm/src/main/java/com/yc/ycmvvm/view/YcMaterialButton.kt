package com.yc.ycmvvm.view

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import com.google.android.material.button.MaterialButton
import com.yc.ycmvvm.R
import com.yc.ycmvvm.extension.ycGetColorRes

open class YcMaterialButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : MaterialButton(context, attrs) {

    private var mBgColorList: MutableList<Int> = mutableListOf()
    private var mTvColorList: MutableList<Int> = mutableListOf()
    private var mStrokeColorList: MutableList<Int> = mutableListOf()
    var mType: Int = 0
        private set

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.YcMaterialButton)
        a.getColor(R.styleable.YcMaterialButton_ycTextColor, ycGetColorRes(R.color.yc_plate_num_text_other)).also {
            mTvColorList.add(it)
        }
        a.getColor(R.styleable.YcMaterialButton_ycTextColor2, ycGetColorRes(R.color.yc_plate_num_text_other)).also {
            mTvColorList.add(it)
        }
        a.getColor(R.styleable.YcMaterialButton_ycTextColor3, ycGetColorRes(R.color.yc_plate_num_text_other)).also {
            mTvColorList.add(it)
        }

        a.getColor(R.styleable.YcMaterialButton_ycBg, ycGetColorRes(R.color.yc_plate_num_text_other)).also {
            mBgColorList.add(it)
        }
        a.getColor(R.styleable.YcMaterialButton_ycBg2, ycGetColorRes(R.color.yc_plate_num_text_other)).also {
            mBgColorList.add(it)
        }
        a.getColor(R.styleable.YcMaterialButton_ycBg3, ycGetColorRes(R.color.yc_plate_num_text_other)).also {
            mBgColorList.add(it)
        }


        a.getColor(R.styleable.YcMaterialButton_ycStrokeColor, ycGetColorRes(R.color.yc_plate_num_text_other)).also {
            mStrokeColorList.add(it)
        }
        a.getColor(R.styleable.YcMaterialButton_ycStrokeColor2, ycGetColorRes(R.color.yc_plate_num_text_other)).also {
            mStrokeColorList.add(it)
        }
        a.getColor(R.styleable.YcMaterialButton_ycStrokeColor3, ycGetColorRes(R.color.yc_plate_num_text_other)).also {
            mStrokeColorList.add(it)
        }
        mType = a.getInt(R.styleable.YcMaterialButton_ycType, 0)
        refreshUI()
        a.recycle()
    }

    fun setType(type: Int) {
        mType = type
        refreshUI()
    }

    fun refreshUI() {
        backgroundTintList = ColorStateList.valueOf(mBgColorList[mType])
        setTextColor(mTvColorList[mType])
        strokeColor = ColorStateList.valueOf(mStrokeColorList[mType])
    }
}