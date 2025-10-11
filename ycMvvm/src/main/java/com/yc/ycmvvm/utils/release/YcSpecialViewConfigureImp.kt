package com.yc.ycmvvm.utils.release

import android.content.Context
import com.yc.ycmvvm.databinding.YcSpecialReleaseBinding
import com.yc.ycmvvm.exception.YcException
import com.yc.ycmvvm.utils.release.YcSpecialViewConfigure
import com.yc.ycmvvm.utils.release.refreshActionbar
import com.yc.ycmvvm.utils.release.refreshContext

/**
 *  通用的一些参数
 */
open class YcSpecialViewConfigureImp(mContext: Context) : YcSpecialViewConfigure<YcSpecialReleaseBinding>(mContext, YcSpecialReleaseBinding::inflate) {

    override fun onUpdate(specialState: Int, exception: YcException?) {
        mViewBinding.apply {
            val isFinish = mPriorityUpdate?.invoke(mViewBinding, specialState, exception)
            if (isFinish != true) {
                this.releaseActionBar.apply {
                    mYcSpecialBean.refreshActionbar(this.root, tvActionbarMid, ivActionbarLeft, tvActionbarRight, ivActionbarRight)
                }
                mYcSpecialBean.refreshContext(specialState, exception, releaseContentTv, releaseButtonBtn, releaseIv)
            }
        }
    }
}