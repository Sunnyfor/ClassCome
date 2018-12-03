package com.sunny.classcome.utils

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Desc
 * Author Justin
 * Mail zhangye98@foxmail.com
 * Date 2018/12/3 22:29
 */
object DigestUtils {

    fun md5(message: String): String {
        try {
            //获取md5加密对象
            val instance: MessageDigest = MessageDigest.getInstance("MD5")
            //对字符串加密，返回字节数组
            val digest = instance.digest(message.toByteArray())
            return bytes2Hex(digest)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }


    /**
     * SHA加密
     * @param strSrc 明文
     * @return 加密之后的密文
     */
    fun sha1(strSrc: String): String {
        return try {
            val md = MessageDigest.getInstance("SHA-1")// 将此换成SHA-1、SHA-512、SHA-384等参数
            val bt = strSrc.toByteArray()
            md.update(bt)
            bytes2Hex(md.digest())
        } catch (e: NoSuchAlgorithmException) {
            ""
        }
    }

    fun sha256(strSrc: String, secret: String): String {
        return try {
            val sha256 = Mac.getInstance("HmacSHA256")
            val secretKey = SecretKeySpec(secret.toByteArray(), "HmacSHA256")
            sha256.init(secretKey)
            val bytes = sha256.doFinal(strSrc.toByteArray())
            return bytes2Hex(bytes)
        } catch (e: NoSuchAlgorithmException) {
            ""
        }
    }


    /**
     * byte数组转换为16进制字符串
     * @param byteArray 数据源
     * @return 16进制字符串
     */
    private fun bytes2Hex(byteArray: ByteArray): String {
        val sb = StringBuffer()
        for (b in byteArray) {
            //获取低八位有效值
            val i: Int = b.toInt() and 0xff
            //将整数转化为16进制
            var hexString = Integer.toHexString(i)
            if (hexString.length < 2) {
                //如果是一位的话，补0
                hexString = "0$hexString"
            }
            sb.append(hexString)
        }
        return sb.toString()
    }
}