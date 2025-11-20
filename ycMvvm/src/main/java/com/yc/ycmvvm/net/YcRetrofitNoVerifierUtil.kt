package com.yc.ycmvvm.net

import android.annotation.SuppressLint
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import kotlin.collections.forEach
/**
 * 网络请求
 * 忽略证书校验
 */
object YcRetrofitNoVerifierUtil {
    @JvmStatic
    @SuppressLint("CustomX509TrustManager", "TrustAllX509TrustManager")
    fun createNoVerifier(baseUrl: String, timeOut: Long = 60, interceptor: List<Interceptor> = listOf()): Retrofit {
        val trustAllCerts = arrayOf<TrustManager>(
            object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
                override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            })

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())
        val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory

        return Retrofit.Builder()
            .client(OkHttpClient.Builder().apply {
                interceptor.forEach {
                    addInterceptor(it)
                }
                addInterceptor(YcInterceptorLog())
                connectTimeout(timeOut, TimeUnit.SECONDS)
                writeTimeout(timeOut, TimeUnit.SECONDS)
                readTimeout(timeOut, TimeUnit.SECONDS)
                followRedirects(true)
                followSslRedirects(true)
                hostnameVerifier { _, _ -> true }
                sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            }.build())
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .baseUrl(baseUrl).build()
    }
}