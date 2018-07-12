package com.eaglesakura.armyknife.runtime.io

import kotlinx.coroutines.experimental.CancellationException
import java.io.IOException
import java.io.InputStream

/**
 * If this function returns true, Input stream read cancel and throw CancellationException.
 */
typealias CancelCallback = () -> Boolean

@Suppress("MemberVisibilityCanBePrivate")
class CancelableInputStream(private val stream: InputStream, private val cancelCallback: CancelCallback, bufferSize: Int = 1024 * 2) : InputStream() {

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

    @Throws(IOException::class)
    override fun read(): Int {
        val buf = ByteArray(1)
        return if (read(buf, 0, 1) < 0) {
            -1
        } else {
            buf[0].toInt() and 0x000000FF
        }
    }

    override fun read(b: ByteArray): Int {
        return read(b, 0, b.size)
    }

    @Throws(IOException::class)
    override fun read(buffer: ByteArray, byteOffset: Int, byteCount: Int): Int {
        throwIfCanceled()

        // キャンセルチェックを容易にするため、一度の取得を小さく保つ
        return stream.read(buffer, byteOffset, Math.min(bufferSize, byteCount))
    }

    @Throws(IOException::class)
    override fun skip(n: Long): Long {
        return stream.skip(n)
    }

    @Throws(IOException::class)
    override fun available(): Int {
        return stream.available()
    }

    @Throws(IOException::class)
    override fun close() {
        stream.close()
    }

    override fun mark(readlimit: Int) {
        stream.mark(readlimit)
    }

    @Throws(IOException::class)
    override fun reset() {
        stream.reset()
    }

    override fun markSupported(): Boolean {
        return stream.markSupported()
    }
}
