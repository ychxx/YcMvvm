package com.yc.ycmvvm.utils

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object YcEncryptionUtils {
    /**
     * MD5加密
     */
    fun toMD5(text: String): String {
        var messageDigest: MessageDigest? = null
        try {
            messageDigest = MessageDigest.getInstance("MD5")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
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
        return sb.toString().substring(8, 24)
    }
}