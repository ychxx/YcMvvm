package com.zrzk.pdaparkingleader.data.net

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.yc.ycmvvm.utils.YcPhoneUtils
import java.io.Serializable

@Keep
data class LoginReq(
    /**
     * 平台id
     */
    @SerializedName("tenantId")
    val tenantId: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String,
//    @SerializedName("deviceId")
//    val deviceId: String = CompatibleUtil.getDeviceId(),
    @SerializedName("clientId")
    var clientId: String = "wsxhu9qjepgnm0o47rckfziy1t58b3dl",
    @SerializedName("grantType")
    val grantType: String = "password"
) : Serializable