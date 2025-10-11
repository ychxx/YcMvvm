package com.yc.ycmvvm.view.spinner

import androidx.viewbinding.ViewBinding

interface YcISpinnerMoreSelectAdapter<Data : Any, VbSelect : ViewBinding, VbDropDown : ViewBinding> : YcISpinnerAdapterBase<Data, VbSelect, VbDropDown> {
    /**
     * 选中限制
     * @return true:可以选中 false:不可选中
     */
    var mSelectLimit: ((clickPosition: Int, clickDate: Data) -> Boolean)?

    /**
     * 设置选中项
     */
    fun setSelectPosition(selectPosition: List<Int>?)

    /**
     * 获取选中的数据
     */
    fun getSelectItem(): List<Data>?

    /**
     * 获取选中的下标
     */
    fun getSelectedItemPosition(): List<Int>?
    /**
     * 展示view更新ui
     */
    var mSelectItemOnUpdate: (VbSelect.(position: List<Int>?, data: List<Data>?) -> Unit)?
}