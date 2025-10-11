package com.yc.ycmvvm.view.spinner

import androidx.viewbinding.ViewBinding

interface YcISpinnerAdapter<Data : Any, VbSelect : ViewBinding, VbDropDown : ViewBinding> : YcISpinnerAdapterBase<Data, VbSelect, VbDropDown> {


    /**
     * 设置选中项
     */
    fun setSelectPosition(selectIndex: Int?)

    /**
     * 获取选中的数据
     */
    fun getSelectItem(): Data?

    /**
     * 获取选中的下标
     */
    fun getSelectedItemPosition(): Int?


    /**
     * 下拉框选中事件
     */
    var mDropDownItemClick: ((item: Data, position: Int) -> Unit)?

    /**
     * 展示view更新ui
     */
    var mSelectItemOnUpdate: (VbSelect.(position: Int?, data: Data?) -> Unit)?
}