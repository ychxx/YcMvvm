package com.yc.jetpacklib.release

import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.yc.ycmvvm.utils.release.YcReleaseLayoutUtils
import com.yc.ycmvvm.utils.release.YcSpecialViewCommon

/**
 *  兼容SmartRefreshLayout
 */
open class YcSpecialViewSmart(originalView: RecyclerView, private val containerFl: FrameLayout?) : YcSpecialViewCommon(originalView) {
    override fun replaceReal() {
        YcReleaseLayoutUtils.replaceSmart(mOriginalView, mReleaseView.invoke(), containerFl)
    }

    override fun recoveryReal() {
        YcReleaseLayoutUtils.recoverySmart(mOriginalView, containerFl)
    }
}