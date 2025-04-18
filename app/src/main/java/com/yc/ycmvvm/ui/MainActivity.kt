package com.yc.ycmvvm.ui

import android.os.Bundle
import com.yc.ycmvvm.adapter.YcRecyclerViewAdapter
import com.yc.ycmvvm.base.YcBaseActivity
import com.yc.ycmvvm.databinding.MainAcBinding
import com.yc.ycmvvm.databinding.MainItemBinding
import com.yc.ycmvvm.extension.ycInitLinearLayoutManage

class MainActivity : YcBaseActivity<MainAcBinding>(MainAcBinding::inflate) {

    private val adapter by lazy {
        YcRecyclerViewAdapter<String, MainItemBinding>(MainItemBinding::inflate).apply {
            mOnUpdate = { data ->
                tv.text = data
            }
        }
    }

    override fun MainAcBinding.initView(savedInstanceState: Bundle?) {
        rv.ycInitLinearLayoutManage()
        rv.adapter = adapter
        adapter.addData("测试输入框")
        adapter.addData("测试CameraX")
        adapter.addData("测试视频播放")
        adapter.mItemClick = {
            when (it) {
                "测试输入框" -> {
                    InputBoxAc.toAc(this@MainActivity)
                }

                "测试CameraX" -> {
                    TestCameraXAc.toAc(this@MainActivity)
                }
                "测试视频播放" -> {
                    TestVideoXAc.toAc(this@MainActivity)
                }

            }
        }
    }
}
