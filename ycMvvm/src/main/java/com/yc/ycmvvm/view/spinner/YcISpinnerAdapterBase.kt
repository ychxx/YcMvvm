package com.yc.ycmvvm.view.spinner

import android.widget.BaseAdapter
import android.widget.ListAdapter
import androidx.viewbinding.ViewBinding

interface YcISpinnerAdapterBase<Data : Any, VbSelect : ViewBinding, VbDropDown : ViewBinding> {

    /**
     * 下拉框 显示隐藏回调
     */
    var mDropDownShowCall: (VbSelect.(hasShow: Boolean) -> Unit)?
    fun spDropDownShowCall(hasShow:Boolean)
    /**
     * 下拉框更新ui
     */
    var mDropDownOnUpdate: (VbDropDown.(position: Int, data: Data, positionSelect: Boolean) -> Unit)?

    /**
     * 绑定的spinner
     */
    var mYcISpinner: YcISpinner?

    /**
     * 获取展示的view
     */
    fun getSelectItemView(): VbSelect


    fun itemClickShowDrop()
    /**
     * 绑定到spinner
     */
    fun onAttachedToSpinnerView(spinner: YcISpinner)

    /**
     * 用于 {@link #YcSpinner 里的mPop }绑定
     */
    fun getListAdapter():ListAdapter
}