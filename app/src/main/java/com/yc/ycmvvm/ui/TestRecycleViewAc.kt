package com.yc.ycmvvm.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.yc.ycmvvm.adapter.YcRecyclerViewAdapter
import com.yc.ycmvvm.base.YcBaseActivity
import com.yc.ycmvvm.databinding.MainItemBinding
import com.yc.ycmvvm.databinding.TestRecycleAcBinding
import com.yc.ycmvvm.extension.ycInitLinearLayoutManage
import java.util.Collections


class TestRecycleViewAc : YcBaseActivity<TestRecycleAcBinding>(TestRecycleAcBinding::inflate) {
    companion object {
        fun toAc(context: Context) {
            context.startActivity(Intent(context, TestRecycleViewAc::class.java))
        }
    }

    val mAdapter by YcRecyclerViewAdapter.ycLazyInitApply<String, MainItemBinding>(MainItemBinding::inflate) {
        mOnUpdate = {
            tv.text = it
        }
    }

    override fun TestRecycleAcBinding.initView(savedInstanceState: Bundle?) {
        rv.ycInitLinearLayoutManage()
        rv.adapter = mAdapter
        for (i in 0..100) {
            mAdapter.addData("第${i}条数据")
        }
        ItemTouchHelper(object : ItemTouchHelper.Callback() {
            var hasMoved = true//用于能否拖拽(用于点击item里的某个按钮场景)
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                if (!hasMoved) {
                    return 0
                }
                val dragFlag = ItemTouchHelper.UP or ItemTouchHelper.DOWN//仅允许上下拖拽
                return makeMovementFlags(dragFlag, 0)
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                if (fromPosition < mAdapter.itemCount && toPosition < mAdapter.itemCount) {
                    //交换数据位置
                    Collections.swap(mAdapter.mData, fromPosition, toPosition)
                    //刷新位置交换
                    mAdapter.notifyItemMoved(fromPosition, toPosition)
                }
                return true
            }

            override fun isItemViewSwipeEnabled(): Boolean {
                return false; //不启用拖拽删除
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            }
        }).attachToRecyclerView(rv)
    }
}
