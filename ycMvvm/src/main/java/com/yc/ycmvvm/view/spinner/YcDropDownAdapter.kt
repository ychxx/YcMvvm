package com.yc.ycmvvm.view.spinner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.viewbinding.ViewBinding

open class YcDropDownAdapter<Data : Any, Vb : ViewBinding>(val vbDropDown: (LayoutInflater, ViewGroup?, Boolean) -> Vb) : BaseAdapter() {
    var mSelectIndex: Int? = null
    open var mDropDownAdapterItemClick: ((item: Data, position: Int, hasDataChange: Boolean) -> Unit)? = null
    open var mDropDownAdapterUpdate: ((vb: Vb, position: Int, data: Data, hasSelect: Boolean) -> Unit)? = null
    var mData: ArrayList<Data> = arrayListOf()
        protected set

    override fun getCount(): Int {
        return mData.size
    }

    override fun getItem(position: Int): Data? {
        return if (position < 0 || position >= mData.size) null else mData[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val vb = vbDropDown.invoke(LayoutInflater.from(parent?.context), parent, false)
        vb.root.setOnClickListener {
            val hasChange = mSelectIndex != position
            mSelectIndex = position
            getItem(position)?.let {
                mDropDownAdapterItemClick?.invoke(it, position, hasChange)
            }
        }
        getItem(position)?.let {
            mDropDownAdapterUpdate?.invoke(vb, position, it, mSelectIndex == position)
        }
        return vb.root
    }


    fun getSelectItem(): Data? {
        return if (mSelectIndex == null) null else getItem(mSelectIndex!!)
    }
}