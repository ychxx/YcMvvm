package com.yc.ycmvvm.utils.release

import android.view.View
import com.yc.ycmvvm.exception.YcException
import com.yc.ycmvvm.utils.release.YcSpecialBean

/**
 *  通用的一些参数
 */
abstract class YcSpecialViewConfigureBase {
    var mYcSpecialBean: YcSpecialBean = YcSpecialBean()

    /**
     * 获取替换用的布局
     */
    abstract fun getSpecialView(): View
    abstract fun onUpdate(specialState: Int, exception: YcException? = null)
}