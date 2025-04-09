package com.yc.ycmvvm.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.yc.ycmvvm.base.YcBaseActivity
import com.yc.ycmvvm.databinding.InputBoxAcBinding

class InputBoxAc : YcBaseActivity<InputBoxAcBinding>(InputBoxAcBinding::inflate) {
    companion object {
        fun toAc(context: Context) {
            context.startActivity(Intent(context, InputBoxAc::class.java))
        }
    }

    override fun InputBoxAcBinding.initView(savedInstanceState: Bundle?) {

    }
}
