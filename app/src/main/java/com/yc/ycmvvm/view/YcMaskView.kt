package com.yc.ycmvvm.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes

class YcMaskView : View {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet? = null) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private val mPaint: Paint by lazy {
        Paint().apply {
            color = mBgColor
            style = Paint.Style.FILL
            strokeWidth = 10f
        }
    }
    var mBgColor: Int = Color.parseColor("#6CAF50FF")
        set(value) {
            field = value
            mPaint.color = value
        }

    var mTransparentWidth = 0f
    var mTransparentHeight = 0f
    fun initView() {

    }

    fun setMask(width: Float, height: Float) {
        mTransparentWidth = width
        mTransparentHeight = height
        invalidate()
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        val path: Path = Path()
        path.addRect(0f, 0f, width.toFloat(), height.toFloat(), Path.Direction.CW)
        // 从全屏路径中"减去"中间透明区域（这里以矩形为例）
        val centerWidth = width / 2f
        val centerHeight = height / 2f
        path.addRect(
            centerWidth - mTransparentWidth / 2,
            centerHeight - mTransparentHeight / 2,
            centerWidth + mTransparentWidth / 2,
            centerHeight + mTransparentHeight / 2,
            Path.Direction.CCW
        )
        canvas.drawPath(path, mPaint)
    }
}