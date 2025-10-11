package com.yc.ycmvvm.view.spinner

import android.view.ViewGroup


interface YcISpinner {
    fun showDropdown()

    fun dismissDropdown()

    fun hasDropdownShowing(): Boolean

    var mAdapter: YcISpinnerAdapterBase<*, *, *>?

    fun setAdapter(adapter: YcISpinnerAdapterBase<*, *, *>)

    fun getViewGroup(): ViewGroup


    var mShowCall: ((hasShow: Boolean) -> Unit)?
}