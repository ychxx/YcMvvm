package com.yc.ycmvvm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.viewbinding.ViewBinding

open class YcBaseAdapter<Data : Any, Vb : ViewBinding>(val createVB: (LayoutInflater, ViewGroup?, Boolean) -> Vb) : BaseAdapter() {

    open var mItemClick: ((item: Data, position: Int) -> Unit)? = null
    open var mOnUpdate: ((vb: Vb, position: Int, data: Data) -> Unit)? = null
    var mData: ArrayList<Data> = arrayListOf()
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
        val vb = createVB.invoke(LayoutInflater.from(parent?.context), parent, false)
        vb.root.setOnClickListener {
            getItem(position)?.also {
                mItemClick?.invoke(it, position)
            }
        }
        getItem(position)?.let {
            mOnUpdate?.invoke(vb, position, it)
        }
        return vb.root
    }
}
