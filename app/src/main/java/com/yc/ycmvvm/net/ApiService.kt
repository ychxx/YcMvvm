package com.yc.ycmvvm.net

import com.yc.ycmvvm.config.YcInit
import com.zrzk.pdaparkingleader.data.net.LoginReq
import com.zrzk.pdaparkingleader.data.net.LoginResp
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    /**
     * 登录(许多设备同时登录)
     */
    @POST("/api/api-admin/login")
    suspend fun login(@Body para: LoginReq?, @Header(YcInit.OTHER_BASE_URL) baseUrl: String): NetResult<LoginResp?>

    /**
     * 退出登录(仅一台设备登录)
     */
    @POST("/api/api-pda/logout")
    suspend fun loginOutOnly(
        @Header(YcInit.OTHER_BASE_URL) baseUrl: String,
        @Header("Authorization") token: String,
        @Header("client-id") id: String = "wsxhu9qjepgnm0o47rckfziy1t58b3dl"
    ): NetResult<Any?>

    /**
     * 测试sse连接
     */
    @POST("test/stream-sse")
    suspend fun streamSse(
        @Header(YcInit.OTHER_BASE_URL) baseUrl: String = "http://192.168.30.122:2320/",
    ): NetResult<Any?>

    /**
     * 测试sse连接
     */
    @GET("/api/childish/memory/setProgress")
    suspend fun setGameProgress(
        @Query("progress") progress: Int,
        @Header(YcInit.OTHER_BASE_URL) baseUrl: String = "https://www.lovenimin.cn/",
    ): NetResult<Any?>
}