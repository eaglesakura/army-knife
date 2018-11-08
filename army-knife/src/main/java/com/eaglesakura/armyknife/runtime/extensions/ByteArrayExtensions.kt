package com.eaglesakura.armyknife.runtime.extensions

import com.eaglesakura.armyknife.runtime.Base64Impl

/**
 * Byte array to base64 encoded string.
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
fun ByteArray.encodeBase64(): String = Base64Impl.byteArrayToString(this)

/**
 * Byte array to hex string.
 * This method returns hex string, it uses characters only "[0-9,a-f]"
 * Alphabet characters are lower-case.
 *
 * example) [0x01, 0x02, FF] -> "0102ff"
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
fun ByteArray.toHexString(): String {
    val sBuffer = StringBuffer(size * 2)
    for (b in this) {
        val s = Integer.toHexString(b.toInt() and 0xff)
        if (s.length == 1) {
            sBuffer.append('0')
        }
        sBuffer.append(s)
    }

    return sBuffer.toString()
}