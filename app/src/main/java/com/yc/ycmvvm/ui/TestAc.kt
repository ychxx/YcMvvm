package com.yc.ycmvvm.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.net.Uri
import android.os.Bundle
import com.yc.ycmvvm.adapter.YcRecyclerViewAdapter
import com.yc.ycmvvm.base.YcBaseActivity
import com.yc.ycmvvm.databinding.CameraxAcBinding
import com.yc.ycmvvm.databinding.MainItemBinding
import com.yc.ycmvvm.databinding.TestAcBinding
import com.yc.ycmvvm.extension.ycInitLinearLayoutManage
import com.yc.ycmvvm.extension.ycLoadImageFile
import com.yc.ycmvvm.extension.ycLogE
import com.yc.ycmvvm.extension.ycLogESimple
import com.yc.ycmvvm.extension.ycShowToast
import com.yc.ycmvvm.net.ApiService
import com.yc.ycmvvm.net.YcRetrofitUtil
import kotlin.jvm.java


class TestAc : YcBaseActivity<TestAcBinding>(TestAcBinding::inflate) {
    companion object {
        fun toAc(context: Context) {
            context.startActivity(Intent(context, TestAc::class.java))
        }
    }


    private val mYcRetrofitUtil by lazy { YcRetrofitUtil.Instance.getApiService(ApiService::class.java) }
    override fun TestAcBinding.initView(savedInstanceState: Bundle?) {
        test0Btn.setOnClickListener {
            ycLaunchIO {
                val a = mYcRetrofitUtil.setGameProgress(0)
                ycShowToast(a.toString())
            }
        }
        test1Btn.setOnClickListener {
            ycLaunchIO {
                val a = mYcRetrofitUtil.setGameProgress(1)
                ycShowToast(a.toString())
            }
        }
        test2Btn.setOnClickListener {
            ycLaunchIO {
                val a = mYcRetrofitUtil.setGameProgress(2)
                ycShowToast(a.toString())
            }
        }
        test3Btn.setOnClickListener {
            ycLaunchIO {
                val a = mYcRetrofitUtil.setGameProgress(3)
                ycShowToast(a.toString())
            }
        }
    }
}
