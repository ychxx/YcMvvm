package com.yc.ycmvvm.utils

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.text.InputFilter.LengthFilter
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.EditText
import com.yc.ycmvvm.config.YcInit

/**
 *
 */
fun Int.ycDp(resources: Resources = YcInit.mInstance.getResources()): Int {
    return (this * resources.displayMetrics.density + 0.5).toInt()
}

fun Float.ycDp(resources: Resources = YcInit.mInstance.getResources()): Float {
    return this * resources.displayMetrics.density + 0.5f
}

fun Double.ycDp(resources: Resources = YcInit.mInstance.getResources()): Double {
    return (this * resources.displayMetrics.density + 0.5)
}

object YcUI {
    /**
     * 将dp值转换为px值
     */
    @JvmStatic
    fun dpToPx(dp: Float, resources: Resources = YcInit.mInstance.getResources()): Int {
        return (resources.displayMetrics.density * dp + 0.5f).toInt()
    }

    @JvmStatic
    fun dip2pxFloat(dp: Float, resources: Resources = YcInit.mInstance.getResources()): Float {
        return resources.displayMetrics.density * dp + 0.5f
    }


    /**
     * 将px值转换为dp值
     */
    @JvmStatic
    fun pxToDp(px: Float, resources: Resources = YcInit.mInstance.getResources()): Int {
        return (px / resources.displayMetrics.density + 0.5f).toInt()
    }

    /**
     * sp转px
     */
    @JvmStatic
    fun spToPx(sp: Float, resources: Resources = YcInit.mInstance.getResources()): Int {
        return (sp * resources.displayMetrics.scaledDensity + 0.5f).toInt()
    }

    /**
     * px转sp
     */
    @JvmStatic
    fun pxToSp(px: Float, resources: Resources = YcInit.mInstance.getResources()): Int {
        return (resources.displayMetrics.scaledDensity / px + 0.5f).toInt()
    }

    /**
     * 获取屏幕宽度 像素值
     */
    @JvmStatic
    fun getScreenWidth(): Int {
        return getDisplayMetrics().widthPixels
    }

    /**
     * 获取屏幕高度 像素值
     */
    @JvmStatic
    fun getScreenHeight(): Int {
        return getDisplayMetrics().heightPixels
    }

    @JvmStatic
    fun getDisplayMetrics(): DisplayMetrics {
        return getDisplayMetrics(YcInit.mInstance.mApplication)
    }

    @JvmStatic
    fun getDisplayMetrics(context: Context): DisplayMetrics {
        val displayMetrics = DisplayMetrics()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics
    }

    @JvmStatic
    fun getMaxLength(editText: EditText): Int {
        var maxLength = 0
        val filters = editText.filters
        filters?.forEach {
            if (it is LengthFilter) {
                maxLength = it.max
            }
        }
        return maxLength
    }
}