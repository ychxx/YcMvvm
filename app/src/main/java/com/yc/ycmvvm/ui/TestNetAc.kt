package com.yc.ycmvvm.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.yc.ycmvvm.R
import com.yc.ycmvvm.base.YcBaseActivity
import com.yc.ycmvvm.config.YcInit
import com.yc.ycmvvm.databinding.TestNetAcBinding
import com.yc.ycmvvm.extension.ycLogE
import com.yc.ycmvvm.net.ApiService
import com.yc.ycmvvm.net.YcRetrofitUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.internal.sse.RealEventSource
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import java.util.concurrent.TimeUnit

class TestNetAc : YcBaseActivity<TestNetAcBinding>(TestNetAcBinding::inflate) {
    companion object {
        fun toAc(context: Context) {
            context.startActivity(Intent(context, TestNetAc::class.java))
        }
    }

    private val mYcRetrofitUtil by lazy { YcRetrofitUtil.Instance.getApiService(ApiService::class.java) }

    override fun TestNetAcBinding.initView(savedInstanceState: Bundle?) {
        testSseBtn.setOnClickListener {
            ycLaunch {
                withContext(Dispatchers.IO) {
                    testSee()
                }
            }
        }
    }

    private fun testSee() {
//        val sseUrl = "http://192.168.30.122:2320/test/stream-sse"
        val sseUrl = ""
        val request = Request.Builder()
            .url(sseUrl)
            .addHeader("lang", "zh_CN")
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "official_test")
            .post("{\"uid\": \"1806980659907006464\",\"prompt\": \"生成一篇关于春天的文章\"}".toRequestBody())
            .build()
        val okHttpClient = OkHttpClient.Builder().also {
            it.connectTimeout(1, TimeUnit.DAYS)
            it.readTimeout(1, TimeUnit.DAYS)
        }.build()
        val event = RealEventSource(request, object : EventSourceListener() {
            override fun onOpen(eventSource: EventSource, response: Response) {
                ycLogE("onOpen: ")
            }

            @SuppressLint("SetTextI18n")
            override fun onEvent(eventSource: EventSource, id: String?, type: String?, data: String) {
                ycLogE("data: $data")
                ycLaunch {
                    mViewBinding.contentTv.text = mViewBinding.contentTv.text.toString() + data
                }
            }

            override fun onClosed(eventSource: EventSource) {
                ycLogE("onClosed: ")
            }

            override fun onFailure(eventSource: EventSource, t: Throwable?, response: Response?) {
                ycLogE("onFailure: $t    $response")
            }
        })
        event.connect(okHttpClient)
    }
}