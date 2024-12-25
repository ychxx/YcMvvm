package com.yc.ycmvvm.view.dialog

interface YcILoading {
    /**
     * 隐藏加载框和文字
     */
    fun loadHide()

    /**
     * 显示加载框和文字
     * @param msg String?
     */
    fun loadShow(msg: String? = null)
}