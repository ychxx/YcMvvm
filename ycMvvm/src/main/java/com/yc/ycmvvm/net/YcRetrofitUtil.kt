package com.yc.ycmvvm.net

import com.yc.ycmvvm.config.YcInit
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
class YcRetrofitUtil private constructor() {
    companion object {
        val Instance = RetrofitUtilHolder.holder

        fun <T> createRetrofitAndGetApiService(service: Class<T>, url: String): T {
            return YcRetrofitUtil().createRetrofit(url).create(service)
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
        val timeout: Int = 60,
        /**
         * retrofit的过滤器
         */
        val interceptor: MutableList<Interceptor> = mutableListOf()
    )

    private object RetrofitUtilHolder {
        val holder = YcRetrofitUtil()
    }

    private var mRetrofit: Retrofit? = null
    private fun createClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            for (interceptor in YcInit.mInstance.mYcRetrofitConfig!!.interceptor) {
                addInterceptor(interceptor)
            }
            addInterceptor(YcInterceptorLog())
            connectTimeout(60, TimeUnit.SECONDS)
            writeTimeout(60, TimeUnit.SECONDS)
            readTimeout(60, TimeUnit.SECONDS)
        }.build()
    }

    private fun createRetrofit(baseUrl: String = YcInit.mInstance.mYcRetrofitConfig!!.mDefaultBaseUrl): Retrofit {
        return Retrofit.Builder()
            .client(createClient())
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }


    fun <T> getApiService(service: Class<T>?): T {
        if (mRetrofit == null) {
            mRetrofit = createRetrofit()
        }
        return mRetrofit!!.create(service)
    }
}