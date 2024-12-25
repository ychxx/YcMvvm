package com.yc.ycmvvm.data.entity

/**
 * Creator: yc
 * Date: 2022/2/22 16:17
 * UseDes:
 */
data class YcLoadingBean(
    val isShow: Boolean,
    val msg: String? = null
) {
    companion object {
        fun show(showMsg: String?): YcLoadingBean {
            return YcLoadingBean(true, showMsg)
        }

        fun hide(): YcLoadingBean {
            return YcLoadingBean(false, null)
        }
    }
}