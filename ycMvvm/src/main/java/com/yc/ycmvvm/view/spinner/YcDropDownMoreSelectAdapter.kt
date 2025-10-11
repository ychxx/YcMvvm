package com.yc.ycmvvm.view.spinner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.viewbinding.ViewBinding

/**
 * 多选的下拉框适配器
 */
open class YcDropDownMoreSelectAdapter<Data : Any, Vb : ViewBinding>(private val vbDropDown: (LayoutInflater, ViewGroup?, Boolean) -> Vb) : BaseAdapter() {
    var mSelectIndexList: HashMap<Int, Boolean> = hashMapOf()
    open var mDropDownAdapterItemClick: (() -> Unit)? = null
    open var mDropDownAdapterUpdate: ((vb: Vb, position: Int, data: Data, hasSelect: Boolean) -> Unit)? = null
    var mData: ArrayList<Data> = arrayListOf()
        protected set

    /**
     * item能否选中,用于限制选中数量
     */
    open var mIsItemSelect: ((clickPosition: Int, clickDate: Data) -> Boolean) = { _, _ -> true }
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
        val hasSelect = mSelectIndexList[position] == true
        vb.root.setOnClickListener {
            val isBeforeSelect = mSelectIndexList[position] == true
            //当未选中改为选中时
            if (isBeforeSelect) {
                //判断数据是否发生改变
                mSelectIndexList[position] = false
                getItem(position)?.let {
                    mDropDownAdapterItemClick?.invoke()
                }
                notifyDataSetChanged()
            } else {
                if (mIsItemSelect.invoke(position, getItem(position)!!)) {
                    mSelectIndexList[position] = true
                    getItem(position)?.let {
                        mDropDownAdapterItemClick?.invoke()
                    }
                    notifyDataSetChanged()
                }
            }
        }
        getItem(position)?.let {
            mDropDownAdapterUpdate?.invoke(vb, position, it, hasSelect)
        }
        return vb.root
    }


    fun getSelectItem(): List<Data>? {
        val list = arrayListOf<Data>()
        mSelectIndexList.forEach { (k, v) ->
            if (v) {
                val item = getItem(k)
                if (item != null) {
                    list.add(item)
                }
            }
        }
        return if (mSelectIndexList.isEmpty()) null else list
    }

    fun getSelectPosition(): List<Int>? {
        return if (mSelectIndexList.isEmpty()) null else mSelectIndexList.filter { it.value }.keys.toList()
    }

}