package com.yc.ycmvvm.view.spinner

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import androidx.appcompat.widget.AppCompatSpinner

open class YcSpinnerSys @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatSpinner(context, attrs) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        dropDownWidth = MeasureSpec.getSize(widthMeasureSpec)
    }

    private var mOpenInitiated = false

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return super.onKeyUp(keyCode, event)
    }

    override fun performClick(): Boolean {
        mOpenInitiated = true
        if (adapter is YcSpinnerSysAdapter<*, *, *>?)
            (adapter as YcSpinnerSysAdapter<*, *, *>?)?.mHasShowDropDown = true
        return super.performClick()
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        if (mOpenInitiated && hasWindowFocus) {
            mOpenInitiated = false
            if (adapter is YcSpinnerSysAdapter<*, *, *>?)
                (adapter as YcSpinnerSysAdapter<*, *, *>?)?.mHasShowDropDown = false
        }
    }

}