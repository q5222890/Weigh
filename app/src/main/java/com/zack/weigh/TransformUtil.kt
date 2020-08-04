package com.zack.weigh

import java.util.*
import kotlin.experimental.and

/**
 * 转换的工具类
 */
object TransformUtil {
    //hex String转byte[]
    /**
     * 16进制表示的字符串转换为字节数组
     *
     * @param s 16进制表示的字符串
     * @return byte[] 字节数组
     */
    fun hexStrToBytes(s: String): ByteArray {
        val len = s.length
        val b = ByteArray(len / 2)
        var i = 0
        while (i < len) {

            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个字节
            b[i / 2] = ((Character.digit(s[i], 16) shl 4) + Character
                    .digit(s[i + 1], 16)).toByte()
            i += 2
        }
        return b
    }

    /**
     * 以十六进制发送指令
     *
     * @param hex
     * @return oh shit!
     */
//    fun hex2bytes(hex: String): ByteArray {
////        String digital = "0123456789ABCDEF";
//        val digital = "0123456789abcdef"
//        val hex1 = hex.replace(" ", "")
//        val hex2char = hex1.toCharArray()
//        val bytes = ByteArray(hex1.length / 2)
//        var temp: Byte
//        for (p in bytes.indices) {
//            temp = ((digital.indexOf(hex2char[2 * p]) * 16).toByte())
//            temp += ((digital.indexOf(hex2char[2 * p + 1])).toByte())
//            bytes[p] = (temp.toInt() and 0xff).toByte()
//        }
//        return bytes
//    }
    //    /**
    //     * 16进制的字符串表示转成字节数组
    //     *
    //     * @param hexString 16进制格式的字符串
    //     * @return 转换后的字节数组
    //     **/
    //    public static byte[] toByteArray(String hexString) {
    //        if (StringUtils.isEmpty(hexString))
    //            throw new IllegalArgumentException("this hexString must not be empty");
    //
    //        hexString = hexString.toLowerCase();
    //        final byte[] byteArray = new byte[hexString.length() / 2];
    //        int k = 0;
    //        for (int i = 0; i < byteArray.length; i++) {
    //            //因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先
    //            byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
    //            byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
    //            byteArray[i] = (byte) (high << 4 | low);
    //            k += 2;
    //        }
    //        return byteArray;
    //    }
    //byte[] 转 hex String
    /**
     * 将字节数组转换为16进制字符串 (大写带空格)
     * @param bytes
     * @return
     */
    fun binaryToHexString(bytes: ByteArray?): String {
        val hexStr = "0123456789ABCDEF"
        var result = ""
        var hex = ""
        for (b in bytes!!) {
            hex = hexStr[b.toInt() and 0xF0 shr 4].toString()
            hex += hexStr[b.toInt() and 0x0F].toString()
            result += "$hex "
        }
        return result
    }

    /**
     * 转十六进制字符串(大写不带空格)
     * @param val
     * @return
     */
    fun bytesToHexStr(`val`: ByteArray): String {
        var temp = ""
        for (i in `val`.indices) {
            var hex = Integer.toHexString(0xff and `val`[i].toInt())
            if (hex.length == 1) { //在个位数补0
                hex = "0$hex"
            }
            temp += hex.toUpperCase() //转大写
        }
        return temp
    }

    /**
     * byte[]数组转换为hex string （大写不带空格）
     *
     * @param data 要转换的字节数组
     * @return 转换后的结果
     */
    fun byteArrayToHexString(data: ByteArray): String {
        val sb = StringBuilder(data.size * 2)
        for (b in data) {
            val v: Int = b.toInt() and 0xff
            if (v < 16) {
                sb.append('0')
            }
            sb.append(Integer.toHexString(v))
        }
        return sb.toString().toUpperCase(Locale.getDefault())
    }

    /**
     * 字节数组转成16进制表示格式的字符串 (小写不带空格)
     *
     * @param byteArray 需要转换的字节数组
     * @return 16进制表示格式的字符串
     */
    fun toHexString(byteArray: ByteArray?): String {
        require(!(byteArray == null || byteArray.size < 1)) { "this byteArray must not be null or empty" }
        val hexString = StringBuilder()
        for (i in byteArray.indices) {
            if (byteArray[i] and 0xff.toByte() < 0x10) //0~F前面补零
                hexString.append("0")
            hexString.append(Integer.toHexString(0xFF and byteArray[i].toInt()))
        }
        return hexString.toString().toLowerCase()
    }

    /**
     * byte[]数组转换为16进制的字符串 （小写）
     *
     * @param bytes 要转换的字节数组
     * @return 转换后的结果
     */
    fun bytesToHexString(bytes: ByteArray): String {
        val sb = StringBuilder()
        for (i in bytes.indices) {
            val hex = Integer.toHexString(0xFF and bytes[i].toInt())
            if (hex.length == 1) {
                sb.append('0')
            }
            sb.append(hex)
        }
        return sb.toString()
    }
}