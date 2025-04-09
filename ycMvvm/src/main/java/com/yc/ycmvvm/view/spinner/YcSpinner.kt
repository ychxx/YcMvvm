package com.yc.ycmvvm.view.spinner

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSpinner

class YcSpinner @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatSpinner(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        dropDownWidth = MeasureSpec.getSize(widthMeasureSpec)
    }
}
