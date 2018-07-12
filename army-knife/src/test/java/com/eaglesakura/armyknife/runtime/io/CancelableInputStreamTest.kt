package com.eaglesakura.armyknife.runtime.io

import com.eaglesakura.BaseTestCase
import kotlinx.coroutines.experimental.CancellationException
import org.junit.Test

import org.junit.Assert.*
import java.io.ByteArrayInputStream

class CancelableInputStreamTest : BaseTestCase() {

    @Test
    fun readOnce() {
        val stream = CancelableInputStream(ByteArrayInputStream(ByteArray(1024)), { false }).also {
            it.bufferSize = 128
        }

        assertEquals(stream.read(ByteArray(512)), 128)
    }

    @Test
    fun readAll() {
        val stream = CancelableInputStream(ByteArrayInputStream(ByteArray(1024)), { false }).also {
            it.bufferSize = 4096
        }

        assertEquals(stream.read(ByteArray(4096)), 1024)
    }

    @Test(expected = CancellationException::class)
    fun readCancel() {
        val stream = CancelableInputStream(ByteArrayInputStream(ByteArray(1024)), { true }).also {
            it.bufferSize = 128
        }
        stream.read()   // throw exception in function.
        fail()
    }
}