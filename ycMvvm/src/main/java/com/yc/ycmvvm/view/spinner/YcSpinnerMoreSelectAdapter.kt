package com.yc.ycmvvm.view.spinner

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.yc.ycmvvm.extension.ycIsNotEmpty
import com.yc.ycmvvm.extension.ycIsTrue

open class YcSpinnerMoreSelectAdapter<Data : Any, VbSelect : ViewBinding, VbDropDown : ViewBinding>(
    private val vbSelect: (LayoutInflater, ViewGroup?, Boolean) -> VbSelect,
    private val vbDropDown: (LayoutInflater, ViewGroup?, Boolean) -> VbDropDown,
) : YcISpinnerMoreSelectAdapter<Data, VbSelect, VbDropDown> {
    protected var mSelectItemView: VbSelect? = null
    internal var mYcSpinner: YcISpinner? = null
    override var mSelectLimit: ((clickPosition: Int, clickDate: Data) -> Boolean)? = null
    override var mDropDownShowCall: (VbSelect.(hasShow: Boolean) -> Unit)? = null
    override var mSelectItemOnUpdate: (VbSelect.(position: List<Int>?, data: List<Data>?) -> Unit)? = null
    override var mDropDownOnUpdate: (VbDropDown.(position: Int, data: Data, positionSelect: Boolean) -> Unit)? = null
    override var mYcISpinner: YcISpinner? = null

    open var mDropDownMoreSelectAdapter: YcDropDownMoreSelectAdapter<Data, VbDropDown> = YcDropDownMoreSelectAdapter<Data, VbDropDown>(vbDropDown).apply {
        mDropDownAdapterUpdate = { vb, position, data, hasSelect ->
            mDropDownOnUpdate?.invoke(vb, position, data, hasSelect)
        }
        mDropDownAdapterItemClick = {
//            mDropDownShowCall?.invoke(mSelectItemView!!, false)
            mSelectItemOnUpdate?.invoke(mSelectItemView!!, this.getSelectPosition(), this.getSelectItem())
        }
        mIsItemSelect = { clickPosition, clickDate ->
            this@YcSpinnerMoreSelectAdapter.mSelectLimit == null || this@YcSpinnerMoreSelectAdapter.mSelectLimit!!.invoke(clickPosition, clickDate)
        }
    }

    override fun spDropDownShowCall(hasShow: Boolean) {
        mDropDownShowCall?.invoke(mSelectItemView!!, hasShow)
    }

    override fun getListAdapter(): ListAdapter {
        return mDropDownMoreSelectAdapter
    }

    override fun getSelectItemView(): VbSelect {
        if (mSelectItemView == null) {
            mSelectItemView = vbSelect.invoke(LayoutInflater.from(mYcSpinner!!.getViewGroup().context), mYcSpinner!!.getViewGroup(), false)
            setSelectPosition(mDropDownMoreSelectAdapter.getSelectPosition())
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
    override fun getSelectedItemPosition(): List<Int>? {
        return mDropDownMoreSelectAdapter.getSelectPosition()
    }

    override fun onAttachedToSpinnerView(spinner: YcISpinner) {
        mYcSpinner = spinner
    }


    override fun getSelectItem(): List<Data>? {
        return mDropDownMoreSelectAdapter.getSelectItem()
    }


    override fun setSelectPosition(selectPosition: List<Int>?) {
        mDropDownMoreSelectAdapter.mSelectIndexList.clear()
        selectPosition?.forEach {
            mDropDownMoreSelectAdapter.mSelectIndexList[it] = true
        }
        if (mSelectItemView != null) {
            mSelectItemOnUpdate?.invoke(
                mSelectItemView!!,
                mDropDownMoreSelectAdapter.getSelectPosition(),
                mDropDownMoreSelectAdapter.getSelectItem()
            )
        }
    }

    fun addAllData(data: List<Data>, hasClear: Boolean = false) {
        if (hasClear) {
            mDropDownMoreSelectAdapter.mData.clear()
        }
        mDropDownMoreSelectAdapter.mData.addAll(data)
    }

    fun addAllAndSetSelect(data: List<Data>?, selectIndex: List<Int>?, hasClear: Boolean = false) {
        if (hasClear) {
            mDropDownMoreSelectAdapter.mData.clear()
        }
        if (data.ycIsNotEmpty()) {
            mDropDownMoreSelectAdapter.mData.addAll(data!!)
        }
        setSelectPosition(selectIndex)
    }

    fun addAllAndSetSelect2(data: List<Data>?, selectData: List<Data>?, hasClear: Boolean = false) {
        if (hasClear) {
            mDropDownMoreSelectAdapter.mData.clear()
        }
        if (data.ycIsNotEmpty()) {
            mDropDownMoreSelectAdapter.mData.addAll(data!!)
        }
        val selectIndex = mutableListOf<Int>()
        for (i in mDropDownMoreSelectAdapter.mData.indices) {
            if (mDropDownMoreSelectAdapter.mData[i] == selectData) {
                selectIndex.add(i)
                break
            }
        }
        setSelectPosition(selectIndex)
    }


    fun clearData() {
        mDropDownMoreSelectAdapter.mData.clear()
        setSelectPosition(null)
    }
}
