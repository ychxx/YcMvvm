package com.yc.ycmvvm.net

import com.yc.ycmvvm.config.YcInit
import com.yc.ycmvvm.data.constans.YcNetErrorCode
import com.yc.ycmvvm.exception.YcException
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Creator: yc
 * Date: 2021/6/8 10:45
 * UseDes:网络请求
 */
class YcRetrofitUtil private constructor(val config: YcRetrofitConfig) {
    companion object {
        lateinit var defaultConfig: YcRetrofitConfig
        val Instance by lazy { createRetrofit(defaultConfig) }

        fun <T> createRetrofitAndGetApiService(service: Class<T>, config: YcRetrofitConfig = defaultConfig): T {
            return YcRetrofitUtil(config).getApiService(service)
        }

        fun createRetrofit(config: YcRetrofitConfig): YcRetrofitUtil {
            return YcRetrofitUtil(config)
        }
    }

    data class YcRetrofitConfig(

        /**
         * 接口地址
         */
        val mDefaultBaseUrl: String,
        /**
         * 超时时间（单位秒）
         */
        val timeout: Long = 60,
        /**
         * retrofit的过滤器
         */
        val interceptor: MutableList<Interceptor> = mutableListOf()
    )

    private val mRetrofit: Retrofit

    init {
        mRetrofit = createRetrofit()
    }

    private fun createClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            config.interceptor.forEach {
                addInterceptor(it)
            }
            addInterceptor(YcInterceptorLog())
            connectTimeout(config.timeout, TimeUnit.SECONDS)
            writeTimeout(config.timeout, TimeUnit.SECONDS)
            readTimeout(config.timeout, TimeUnit.SECONDS)
        }.build()
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .client(createClient())
            .baseUrl(config.mDefaultBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    fun <T> getApiService(service: Class<T>): T {
        return mRetrofit.create(service)
    }
}