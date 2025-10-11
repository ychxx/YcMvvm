package com.yc.ycmvvm.utils.release

import android.app.Activity
import android.view.View
import com.yc.ycmvvm.config.YcInit
import com.yc.ycmvvm.exception.YcException

/**
 *  通用的网络异常，空数据等展示页
 */
open class YcSpecialViewCommon : YcSpecialViewBase {
    val mSpecialViewBuild: YcSpecialViewConfigureBase

    constructor(
        originalView: View,
        specialViewBuild: YcSpecialViewConfigureBase = YcInit.mInstance.mCreateSpecialViewBuildBase.invoke(originalView.context)
    ) : super(originalView, { specialViewBuild.getSpecialView() }) {
        mSpecialViewBuild = specialViewBuild
    }

    constructor(
        activity: Activity, specialViewBuild: YcSpecialViewConfigureBase = YcInit.mInstance.mCreateSpecialViewBuildBase.invoke(activity)
    ) : super(activity, { specialViewBuild.getSpecialView() }) {
        mSpecialViewBuild = specialViewBuild
    }

    override fun onUpdate(specialState: Int, exception: YcException?) {
        mSpecialViewBuild.onUpdate(specialState, exception)
    }

}