package com.zrzk.pdaparkingleader.data.net

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable
@Keep
data class LoginResp(
    @SerializedName("access_token")
    val token: String?
): Serializable