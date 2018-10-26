package com.eaglesakura.armyknife.runtime.io

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eaglesakura.armyknife.android.junit4.extensions.compatibleTest
import kotlinx.coroutines.CancellationException
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import java.io.ByteArrayInputStream

@RunWith(AndroidJUnit4::class)
class CancelableInputStreamTest {

    @Test
    fun readOnce() = compatibleTest {
        val stream = CancelableInputStream(ByteArrayInputStream(ByteArray(1024)), { false }).also {
            it.bufferSize = 128
        }

        assertEquals(stream.read(ByteArray(512)), 128)
    }

    @Test
    fun readAll() = compatibleTest {
        val stream = CancelableInputStream(ByteArrayInputStream(ByteArray(1024)), { false }).also {
            it.bufferSize = 4096
        }

        assertEquals(stream.read(ByteArray(4096)), 1024)
    }

    @Test(expected = CancellationException::class)
    fun readCancel() = compatibleTest {
        val stream = CancelableInputStream(ByteArrayInputStream(ByteArray(1024)), { true }).also {
            it.bufferSize = 128
        }
        stream.read()   // throw exception in function.
        fail()
    }
}