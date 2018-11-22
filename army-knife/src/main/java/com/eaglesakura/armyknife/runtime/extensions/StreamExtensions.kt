package com.eaglesakura.armyknife.runtime.extensions

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.yield
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * Read all data from InputStream.
 * This method supported for Cancel in read process.
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
@Throws(IOException::class)
suspend fun InputStream.readBytesInCoroutines(readOnce: Int = 1024): ByteArray {
    val out = ByteArrayOutputStream(readOnce)
    val buffer = ByteArray(readOnce)

    do {
        val bytes = read(buffer)
        if (bytes < 0) {
            yield()
            return out.toByteArray()
        } else {
            yield()
            out.write(buffer, 0, bytes)
        }
    } while (true)
}

/**
 * Read all data from InputStream.
 * This method supported for Cancel in read process.
 *
 * If cancelCallback() returns true, then throw CancellationException in this function.
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
@Throws(CancellationException::class, IOException::class)
fun InputStream.readBytes(readOnce: Int, cancelCallback: CancelCallback): ByteArray {
    val out = ByteArrayOutputStream(1024)
    val buffer = ByteArray(readOnce)

    // validate not cancel.
    val assertion = {
        if (cancelCallback()) {
            throw CancellationException("read canceled")
        }
    }

    do {
        val bytes = read(buffer)
        if (bytes < 0) {
            return out.toByteArray()
        } else {
            assertion()
            out.write(buffer, 0, bytes)
        }
        assertion()
    } while (true)
}