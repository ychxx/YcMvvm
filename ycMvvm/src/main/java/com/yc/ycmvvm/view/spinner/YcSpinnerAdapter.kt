package com.yc.ycmvvm.view.spinner

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.yc.ycmvvm.extension.ycIsNotEmpty
import com.yc.ycmvvm.extension.ycIsTrue

open class YcSpinnerAdapter<Data : Any, VbSelect : ViewBinding, VbDropDown : ViewBinding>(
    private val vbSelect: (LayoutInflater, ViewGroup?, Boolean) -> VbSelect,
    private val vbDropDown: (LayoutInflater, ViewGroup?, Boolean) -> VbDropDown,
) : YcISpinnerAdapter<Data, VbSelect, VbDropDown> {
    protected var mSelectItemView: VbSelect? = null
    internal var mYcSpinner: YcISpinner? = null

    override var mDropDownShowCall: (VbSelect.(hasShow: Boolean) -> Unit)? = null
    override var mSelectItemOnUpdate: (VbSelect.(position: Int?, data: Data?) -> Unit)? = null
    override var mDropDownOnUpdate: (VbDropDown.(position: Int, data: Data, positionSelect: Boolean) -> Unit)? = null
    override var mDropDownItemClick: ((item: Data, position: Int) -> Unit)? = null
    override var mYcISpinner: YcISpinner? = null

    open var mDropDownAdapter: YcDropDownAdapter<Data, VbDropDown> = YcDropDownAdapter<Data, VbDropDown>(vbDropDown).apply {
        mDropDownAdapterUpdate = { vb, position, data, hasSelect ->
            mDropDownOnUpdate?.invoke(vb, position, data, hasSelect)
        }
        mDropDownAdapterItemClick = { item, position, hasDataChange ->
            if (hasDataChange) {
                mDropDownItemClick?.invoke(item, position)
//                mDropDownShowCall?.invoke(mSelectItemView!!, false)
                mSelectItemOnUpdate?.invoke(mSelectItemView!!, position, item)
            }
            dismissDropdown()
        }
    }

    override fun spDropDownShowCall(hasShow: Boolean) {
        mDropDownShowCall?.invoke(mSelectItemView!!, hasShow)
    }

    override fun getListAdapter(): ListAdapter {
        return mDropDownAdapter
    }

    override fun getSelectItemView(): VbSelect {
        if (mSelectItemView == null) {
            mSelectItemView = vbSelect.invoke(LayoutInflater.from(mYcSpinner!!.getViewGroup().context), mYcSpinner!!.getViewGroup(), false)
            setSelectPosition(mDropDownAdapter.mSelectIndex)
        }
        mSelectItemView!!.root.setOnClickListener {
            itemClickShowDrop()
        }
        return mSelectItemView!!
    }

    override fun itemClickShowDrop() {
        if (mYcSpinner?.hasDropdownShowing().ycIsTrue()) {
            dismissDropdown()
        } else {
            showDropdown()
        }
    }

    private fun dismissDropdown() {
//        mDropDownShowCall?.invoke(mSelectItemView!!, false)
        mYcSpinner?.dismissDropdown()
    }

    private fun showDropdown() {
//        mDropDownShowCall?.invoke(mSelectItemView!!, true)
        mYcSpinner?.showDropdown()
    }

    override fun getSelectedItemPosition(): Int? {
        return mDropDownAdapter.mSelectIndex
    }

    override fun onAttachedToSpinnerView(spinner: YcISpinner) {
        mYcSpinner = spinner
    }


    override fun getSelectItem(): Data? {
        return mDropDownAdapter.getSelectItem()
    }


    override fun setSelectPosition(selectIndex: Int?) {
        if (mDropDownAdapter.mSelectIndex != selectIndex) {
            mDropDownAdapter.mSelectIndex = selectIndex
        }
        if (mSelectItemView != null) {
            mSelectItemOnUpdate?.invoke(mSelectItemView!!, mDropDownAdapter.mSelectIndex, mDropDownAdapter.getSelectItem())
        }
    }

    fun addAllData(data: List<Data>, hasClear: Boolean = false) {
        if (hasClear) {
            mDropDownAdapter.mData.clear()
        }
        mDropDownAdapter.mData.addAll(data)
    }

    fun addAllAndSetSelect(data: List<Data>?, selectIndex: Int?, hasClear: Boolean = false) {
        if (hasClear) {
            mDropDownAdapter.mData.clear()
        }
        if (data.ycIsNotEmpty()) {
            mDropDownAdapter.mData.addAll(data!!)
        }
        setSelectPosition(selectIndex)
    }

    fun addAllAndSetSelect(data: List<Data>?, selectData: Data?, hasClear: Boolean = false) {
        if (hasClear) {
            mDropDownAdapter.mData.clear()
        }
        if (data.ycIsNotEmpty()) {
            mDropDownAdapter.mData.addAll(data!!)
        }
        var selectIndex = 0
        for (i in mDropDownAdapter.mData.indices) {
            if (mDropDownAdapter.mData[i] == selectData) {
                selectIndex = i
                break
            }
        }
        setSelectPosition(selectIndex)
    }


    fun clearData() {
        mDropDownAdapter.mData.clear()
        setSelectPosition(null)
    }
}
