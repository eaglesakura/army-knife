package com.eaglesakura.armyknife.runtime.extensions

import kotlinx.coroutines.experimental.CancellationException
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * Read all data from InputStream.
 * This method supported for Cancel in read process.
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