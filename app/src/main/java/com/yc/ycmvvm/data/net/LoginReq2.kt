package com.yc.ycmvvm.data.net

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
data class LoginReq2(
    /**
     * 平台id
     */
    @SerializedName("userName")
    val tenantId: String = "admin",
    @SerializedName("password")
    val username: String = "201601",
) : Serializable