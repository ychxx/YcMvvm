package com.yc.ycmvvm.view.dialog

interface YcIDialog<T> {
    fun setMsg(msg: String?): T
    fun setLeftBtnText(leftBtnText: String?): T
    fun setOnLeftClick(onLeftClick: YcOnClick): T
    fun setRightBtnText(rightBtnText: String?): T
    fun setOnRightClick(onRightClick: YcOnClick): T
    fun show()
}