package com.eaglesakura.armyknife.runtime.extensions

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch

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

/**
 * Send element to channel without coroutines.
 */
fun <E> SendChannel<E>.send(dispatcher: CoroutineDispatcher, element: E) {
    GlobalScope.launch(dispatcher) {
        send(element)
    }
}

/**
 * Cancel channel by Error.
 *
 * e.g.)
 * val channel = Channel<String>()
 *
 * doInCoroutines {
 *      try {
 *          channel.send("Done")
 *      } catch(e: Throwable) {
 *          channel.cancelByError(e)
 *      }
 * }
 *
 * channel.receiveOrError()
 *
 * @see Channel.receiveOrError
 */
fun <E> Channel<E>.cancelByError(e: Throwable) {
    cancel(ErrorCancelException(e))
}

/**
 * Cancel channel by Error.
 *
 * e.g.)
 * val channel = Channel<String>()
 *
 * doInCoroutines {
 *      try {
 *          channel.send("Done")
 *      } catch(e: Throwable) {
 *          channel.cancelByError(e)
 *      }
 * }
 *
 * channel.receiveOrError()
 *
 * @see Channel.cancelByError
 */
suspend fun <E> Channel<E>.receiveOrError(): E {
    try {
        return receive()
    } catch (e: ErrorCancelException) {
        throw e.cause
    }
}

internal class ErrorCancelException(
    override val cause: Throwable
) : CancellationException()