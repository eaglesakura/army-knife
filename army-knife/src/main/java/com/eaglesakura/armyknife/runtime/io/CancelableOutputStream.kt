package com.eaglesakura.armyknife.runtime.io

import com.eaglesakura.armyknife.runtime.extensions.CancelCallback
import kotlinx.coroutines.CancellationException
import java.io.OutputStream

/**
 * add cancellation spec to OutputStream.
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
class CancelableOutputStream(
    private val stream: OutputStream,
    private val cancelCallback: CancelCallback,
    bufferSize: Int = 1024 * 2
) : OutputStream() {

    var bufferSize = bufferSize
        set(value) {
            if (value <= 0) {
                throw IllegalArgumentException()
            }
            field = value
        }

    private fun throwIfCanceled() {
        if (cancelCallback()) {
            throw CancellationException()
        }
    }

    override fun write(bytes: Int) {
        throwIfCanceled()
        stream.write(bytes)
    }

    override fun write(bytes: ByteArray) {
        throwIfCanceled()
        write(bytes, 0, bytes.size)
    }

    override fun write(bytes: ByteArray, off: Int, len: Int) {
        throwIfCanceled()

        var cursor = off
        var length = len

        while (length > 0) {
            throwIfCanceled()
            if (length < bufferSize) {
                stream.write(bytes, cursor, length)
                return
            } else {
                stream.write(bytes, cursor, bufferSize)
                cursor += bufferSize
                length -= bufferSize
            }
        }
    }

    override fun flush() {
        throwIfCanceled()
        stream.flush()
    }

    override fun close() {
        stream.close()
    }
}