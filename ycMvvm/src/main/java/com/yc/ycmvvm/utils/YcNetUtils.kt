package com.yc.ycmvvm.utils

import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

object YcNetUtils {
    fun checkUrl(url: String): Boolean {
        return try {
            val parseUrl = url.toHttpUrlOrNull()
            parseUrl != null
        } catch (e: Exception) {
            false
        }
    }
}