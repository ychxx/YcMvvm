package com.yc.ycmvvm.utils

import java.security.MessageDigest

object YcEncryptionUtils {
    /**
     * MD5加密
     */
    fun toMD5(text: String): String {
        return try {
            val messageDigest: MessageDigest? = MessageDigest.getInstance("MD5")
            val digest = messageDigest!!.digest(text.toByteArray())
            val sb = java.lang.StringBuilder()
            for (i in digest.indices) {
                val digestInt = digest[i].toInt() and 0xff
                //将10进制转化为较短的16进制
                val hexString = Integer.toHexString(digestInt)
                if (hexString.length < 2) {
                    sb.append(0)
                }
                sb.append(hexString)
            }
            sb.toString().substring(8, 24)
        } catch (e: Exception) {
            e.printStackTrace()
            text
        }
    }
}