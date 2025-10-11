package com.yc.ycmvvm.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.yc.ycmvvm.base.YcBaseActivity
import com.yc.ycmvvm.databinding.TestQrAcBinding
import com.yc.ycmvvm.extension.ycLogE
import com.yc.ycmvvm.extension.ycShowToast


class TestQrAc : YcBaseActivity<TestQrAcBinding>(TestQrAcBinding::inflate) {
    companion object {
        fun toAc(context: Context) {
            context.startActivity(Intent(context, TestQrAc::class.java))
        }
    }

    override fun TestQrAcBinding.initView(savedInstanceState: Bundle?) {
        createQrBtn.setOnClickListener {
            ycLogE("触发点击")
            ycShowToast("触发点击")
        }
    }
}
