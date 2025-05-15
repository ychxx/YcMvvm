package com.yc.ycmvvm.view.spinner

import androidx.viewbinding.ViewBinding

interface YcISpinnerAdapter<Data : Any, VbSelect : ViewBinding, VbDropDown : ViewBinding> {
    /**
     * 下拉框适配器
     */
    var mDropDownAdapter: YcDropDownAdapter<Data, VbDropDown>

    /**
     * 下拉框 显示隐藏回调
     */
    var mDropDownShowCall: (VbSelect.(hasShow: Boolean) -> Unit)?

    /**
     * 展示view更新ui
     */
    var mSelectItemOnUpdate: (VbSelect.(position: Int?, data: Data?) -> Unit)?

    /**
     * 下拉框更新ui
     */
    var mDropDownOnUpdate: (VbDropDown.(position: Int, data: Data, positionSelect: Boolean) -> Unit)?

    /**
     * 下拉框选中事件
     */
    var mDropDownItemClick: ((item: Data, position: Int) -> Unit)?


    /**
     * 绑定的spinner
     */
    var mYcISpinner: YcISpinner?

    /**
     * 获取展示的view
     */
    fun getSelectItemView(): VbSelect

    /**
     * 设置选中项
     */
    fun setSelectPosition(selectIndex: Int?)

    /**
     * 获取选中的下标
     */
    fun getSelectedItemPosition(): Int?

    /**
     * 获取选中的数据
     */
    fun getSelectItem(): Data?

    /**
     * 绑定到spinner
     */
    fun onAttachedToSpinnerView(spinner: YcISpinner)
}