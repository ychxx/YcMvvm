package com.yc.ycmvvm.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import com.yc.ycmvvm.R
import com.yc.ycmvvm.extension.ycGetColorRes
import com.yc.ycmvvm.extension.ycIsNotEmpty
import com.yc.ycmvvm.extension.ycToNoEmpty
import kotlin.math.PI
import kotlin.math.atan2

class YcCircleRingView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {
    private val CORRECT_ANGLE = -90 // 起始角度（默认从顶部开始）

    @ColorInt
    private var mBgColor: Int

    /**
     * 环的有效颜色
     */
    @ColorInt
    private var mRingColorEffective: Int

    /**
     * 环的背景颜色
     */
    @ColorInt
    private var mRingColorBg: Int

    /**
     * 环所在整体的比例
     */
    private var mRingWithRate: Float = 0.2f


    /**
     * 百分比进度
     */
    private var mProgress: Float

    private val mPaintRing: Paint
    private val mPaintText: Paint
    private var mText: String
    private var mTextSize: Float
    var mHasTextBold: Boolean = false
        set(value) {
            field = value
            mPaintRing.typeface = if (value) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
            invalidate()
        }

    /**
     * 文字颜色
     */
    @ColorInt
    private var mTextColor: Int
    var useRoundCap: Boolean = false
        set(value) {
            field = value
            mPaintRing.strokeCap = if (value) Paint.Cap.ROUND else Paint.Cap.BUTT
            invalidate()
        }

    init {
        // 硬件加速不支持，图层混合。
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        val a = context.obtainStyledAttributes(attrs, R.styleable.YcRingView)
        mBgColor = a.getColor(R.styleable.YcRingView_ycBg, ycGetColorRes(R.color.yc_transparent))
        mRingColorEffective = a.getColor(R.styleable.YcRingView_ycRingColorEffective, Color.RED)
        mRingColorBg = a.getColor(R.styleable.YcRingView_ycRingColorBg, Color.BLUE)
        mRingWithRate = a.getFloat(R.styleable.YcRingView_ycRingWithRate, 0.2f)
        mProgress = a.getFloat(R.styleable.YcRingView_ycProgress, 0.5f)

        mPaintRing = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaintRing.style = Paint.Style.STROKE
        mPaintRing.isAntiAlias = true
        mHasTextBold = a.getBoolean(R.styleable.YcRingView_ycHasTextBold, false)

        mText = a.getString(R.styleable.YcRingView_ycText).ycToNoEmpty("")
        mTextSize = a.getDimension(R.styleable.YcRingView_ycTextSize, 22f)
        mTextColor = a.getColor(R.styleable.YcRingView_ycTextColor, Color.BLACK)
        mPaintText = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaintText.style = Paint.Style.FILL
        mPaintText.isAntiAlias = true
        mPaintText.textSize = mTextSize
//        mPaintText.isFakeBoldText = a.getBoolean(R.styleable.YcRingView_ycTextSize, false)
        a.recycle()
    }

    fun setTypeface(tf: Typeface) {
        if (mPaintText.typeface !== tf) {
            mPaintText.typeface = tf
            requestLayout()
            invalidate()
        }
    }

    /**
     * 设置进度（0%~100%）
     */
    fun setProgress(progress: Float) {
        mProgress = progress
        invalidate()
    }

    private var rectF: RectF? = null
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // 计算绘制区域（考虑 padding）
        val paddingLeft = paddingLeft.toFloat()
        val paddingRight = paddingRight.toFloat()
        val paddingTop = paddingTop.toFloat()
        val paddingBottom = paddingBottom.toFloat()

        val width = w - paddingLeft - paddingRight
        val height = h - paddingTop - paddingBottom
        val halfStroke = (mRingWithRate * width / 2f) / 2f

        rectF = RectF(
            paddingLeft + halfStroke,
            paddingTop + halfStroke,
            paddingLeft + width - halfStroke,
            paddingTop + height - halfStroke
        )
    }

    override fun draw(canvas: Canvas) {
        canvas.apply {
            this.drawColor(mBgColor)
            rectF?.let {
                val ringWidth = (mRingWithRate * width / 2f)
                mPaintRing.strokeWidth = ringWidth
                val out = if (useRoundCap) {
                    (atan2(ringWidth / 2.0, (ringWidth / 2.0 + ((1 - mRingWithRate) * width / 2f))) * 180f / PI).toFloat()
                } else {
                    0f
                }

                mPaintRing.color = mRingColorBg
                drawArc(it, CORRECT_ANGLE + out, 360f - out * 2, false, mPaintRing)
                mPaintRing.color = mRingColorEffective
                drawArc(it, CORRECT_ANGLE + out, mProgress * 360 - out * 2, false, mPaintRing)
            }
            if (mText.ycIsNotEmpty()) {
                val textRectSum = Rect()
                val textSum = mText
                mPaintText.getTextBounds(textSum, 0, textSum.length, textRectSum)
                val textWidthSum = textRectSum.width()
                val textHeightSum = textRectSum.height()
                val startX = (width - textWidthSum) / 2f
                val startY = (height + textHeightSum) / 2f
                mPaintText.color = mTextColor
                drawText(mText, startX, startY, mPaintText)
            }
        }
        super.draw(canvas)
    }

    fun setText(text: String) {
        mText = text
        invalidate()
    }
}