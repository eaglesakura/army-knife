package com.eaglesakura.armyknife.runtime.extensions

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel

/**
 * Channel close on exit.
 *
 * e.g.)
 * val channel: Channel<Int> = ...
 * channel.use {
 *      val value = receive()
 *      // do something.
 * }    // close on exit.
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
suspend fun <R, T> Channel<T>.use(block: suspend (channel: ReceiveChannel<T>) -> R): R {
    try {
        return block(this)
    } finally {
        close()
    }
}