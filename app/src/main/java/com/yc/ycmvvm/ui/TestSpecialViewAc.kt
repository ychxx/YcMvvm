package com.yc.ycmvvm.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.yc.ycmvvm.adapter.YcRecyclerViewAdapter
import com.yc.ycmvvm.base.YcBaseActivity
import com.yc.ycmvvm.databinding.MainItemBinding
import com.yc.ycmvvm.databinding.TestSpecialViewAcBinding
import com.yc.ycmvvm.databinding.TestSpecialViewEmptyBinding
import com.yc.ycmvvm.extension.ycInitLinearLayoutManage
import com.yc.ycmvvm.utils.release.YcReleaseLayoutUtils

class TestSpecialViewAc : YcBaseActivity<TestSpecialViewAcBinding>(TestSpecialViewAcBinding::inflate) {
    companion object {
        fun toAc(context: Context) {
            context.startActivity(Intent(context, TestSpecialViewAc::class.java))
        }
    }

    private val specialLayoutEmpty by lazy { TestSpecialViewEmptyBinding.inflate(LayoutInflater.from(this)) }
    private var hasEmpty = true
    private val adapter by lazy {
        YcRecyclerViewAdapter<String, MainItemBinding>(MainItemBinding::inflate).apply {
            mOnUpdate = { data ->
                tv.text = data
            }
        }
    }

    override fun TestSpecialViewAcBinding.initView(savedInstanceState: Bundle?) {
        rv.ycInitLinearLayoutManage()
        rv.adapter = adapter

        searchBtn.setOnClickListener {
            if (hasEmpty) {
                adapter.clearData()
                YcReleaseLayoutUtils.replace(rv, specialLayoutEmpty.root)
                hasEmpty = false
            } else {
                YcReleaseLayoutUtils.recovery(rv)
                adapter.addAllData(listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"), true)
                hasEmpty = true
            }
        }
    }
}