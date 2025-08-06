package com.yc.ycmvvm.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.yc.ycmvvm.adapter.YcRecyclerViewAdapter
import com.yc.ycmvvm.base.YcBaseActivity
import com.yc.ycmvvm.databinding.TestNetAcBinding
import com.yc.ycmvvm.extension.toYcException
import com.yc.ycmvvm.extension.ycShowToast
import com.yc.ycmvvm.net.ApiService
import com.yc.ycmvvm.net.YcRetrofitUtil
import com.yc.ycmvvm.net.checkCode
import com.zrzk.pdaparkingleader.data.net.LoginReq

class TestNetAc : YcBaseActivity<TestNetAcBinding>(TestNetAcBinding::inflate) {
    companion object {
        fun toAc(context: Context) {
            context.startActivity(Intent(context, TestNetAc::class.java))
        }
    }

    private val mYcRetrofitUtil by lazy { YcRetrofitUtil.Instance.getApiService(ApiService::class.java) }

    override fun TestNetAcBinding.initView(savedInstanceState: Bundle?) {
        plateCodeEdt.setText("30000001")
        userNameEdt.setText("android3")
        userPwsEdt.setText("123456")
        loginOutBtn.setOnClickListener {
            val plateCode = plateCodeEdt.text
            val userName = userNameEdt.text
            val userPws = userPwsEdt.text
            val baseUrl = if (testRdb.isChecked) "http://manager.parking.test.zrzkwlw.com/" else "https://parking.zrzkwlw.com/"
//            val baseUrl = if (testRdb.isChecked) "http://manager.parking.dev.zrzkwlw.com/" else "https://parking.zrzkwlw.com/"
            ycLaunch {
                try {
                    val loginResp = mYcRetrofitUtil.login(
                        LoginReq(
                            plateCode.toString(),
                            userName.toString(),
                            userPws.toString()
                        ), baseUrl
                    ).checkCode()

                    mYcRetrofitUtil.loginOutOnly(baseUrl, "Bearer ${loginResp!!.token!!}").checkCode()
                    ycShowToast("退出成功")
                }catch (e:Exception){
                    e.toYcException().ycShowNetError()
                }
            }
        }
    }

}