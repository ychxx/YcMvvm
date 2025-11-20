package com.yc.ycmvvm.ui

import android.os.Bundle
import com.yc.ycmvvm.base.YcBaseActivity
import com.yc.ycmvvm.databinding.TestWordOrPdfAcBinding

class TestWordOrPdfAc : YcBaseActivity<TestWordOrPdfAcBinding>(TestWordOrPdfAcBinding::inflate){
    override fun TestWordOrPdfAcBinding.initView(savedInstanceState: Bundle?) {
        openPdfBtn.setOnClickListener {

        }
    }
}