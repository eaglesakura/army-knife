package com.eaglesakura.armyknife.runtime

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eaglesakura.armyknife.android.junit4.extensions.compatibleTest
import com.eaglesakura.armyknife.runtime.extensions.toHexString
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ByteArrayExtensionsTest {
    @Test
    fun toHex() = compatibleTest {
        byteArrayOf().toHexString().also {
            assertEquals("", it)
        }
        byteArrayOf(0x01, 0x02, 0xFF.toByte()).toHexString().also {
            assertEquals("0102ff", it)
        }
    }
}