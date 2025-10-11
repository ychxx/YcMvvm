package com.yc.ycmvvm.view.spinner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.SpinnerAdapter
import androidx.appcompat.widget.AppCompatSpinner
import androidx.viewbinding.ViewBinding

class YcSpinnerSysAdapter<Data : Any, VB : ViewBinding, VB2 : ViewBinding>(
    val vbSelect: (LayoutInflater, ViewGroup?, Boolean) -> VB,
    val vbDropDown: (LayoutInflater, ViewGroup?, Boolean) -> VB2
) : BaseAdapter(), SpinnerAdapter {
    open var mData: MutableList<Data> = mutableListOf()
        protected set

    open var mOnDropDownUpdate: (VB2.(data: Data) -> Unit)? = null
    open var mOnUpdate: (VB.(data: Data, hasShowDropDown: Boolean) -> Unit)? = null

    override fun getCount(): Int {
        return mData.size
    }

    override fun getItem(position: Int): Data {
        return mData[position]
    }


    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return vbSelect.invoke(LayoutInflater.from(parent?.context), parent, false).apply {
            getItem(position).let { mOnUpdate?.invoke(this, it, mHasShowDropDown) }
        }.root
    }

    var mHasShowDropDown: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return vbDropDown.invoke(LayoutInflater.from(parent?.context), parent, false).apply {
            getItem(position).let { mOnDropDownUpdate?.invoke(this, it) }
        }.root
    }

    fun addAllData(data: List<Data>?, isClear: Boolean = true) {
        if (isClear) {
            mSelectIndex = 0
            mData.clear()
        }
        if (data == null) {
            mSelectIndex = 0
            mData.clear()
        } else {
            mData.addAll(data)
        }
        notifyDataSetChanged()
        mSpinner?.setSelection(0)
    }

    var mSelectIndex = 0
    var mSpinner: AppCompatSpinner? = null
    var mSelectCall: ((data: Data) -> Unit)? = null
    fun setAttachedSp(sp: AppCompatSpinner) {
        mSpinner = sp
        sp.adapter = this
        sp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mSelectIndex = position
                mSelectCall?.invoke(getItem(position))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

    }

    fun getSelectItem(): Data? {
        return if (mData.isNotEmpty()) {
            mData[mSelectIndex]
        } else {
            null
        }
    }
}