package com.yc.ycmvvm.view.spinner

import android.view.ViewGroup


interface YcISpinner {
    fun showDropdown()

    fun dismissDropdown()

    fun hasDropdownShowing(): Boolean

    var mAdapter: YcISpinnerAdapter<*, *, *>?

    fun setAdapter(adapter: YcISpinnerAdapter<*, *, *>)

    fun getViewGroup(): ViewGroup
}