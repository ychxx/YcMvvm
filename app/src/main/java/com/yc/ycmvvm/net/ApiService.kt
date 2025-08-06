package com.yc.ycmvvm.net

import com.yc.ycmvvm.config.YcInit
import com.zrzk.pdaparkingleader.data.net.LoginReq
import com.zrzk.pdaparkingleader.data.net.LoginResp
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

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

}