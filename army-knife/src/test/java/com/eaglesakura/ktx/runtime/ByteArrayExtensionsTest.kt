package com.eaglesakura.ktx.runtime

import com.eaglesakura.BaseTestCase
import com.eaglesakura.ktx.runtime.extensions.toHexString
import org.junit.Assert.*
import org.junit.Test

class ByteArrayExtensionsTest : BaseTestCase() {
    @Test
    fun toHex() {
        byteArrayOf().toHexString().also {
            assertEquals("", it)
        }
        byteArrayOf(0x01, 0x02, 0xFF.toByte()).toHexString().also {
            assertEquals("0102ff", it)
        }
    }
}