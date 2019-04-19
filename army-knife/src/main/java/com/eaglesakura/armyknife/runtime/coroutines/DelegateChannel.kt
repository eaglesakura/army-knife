package com.eaglesakura.armyknife.runtime.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ChannelIterator
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.selects.SelectClause1
import kotlinx.coroutines.selects.SelectClause2

/**
 * Delegate supported channel.
 *
 * If you want to the simple use case, then replace to "RendezvousChannel<T>".
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
abstract class DelegateChannel<T>(
    @Suppress("MemberVisibilityCanBePrivate")
    protected val origin: Channel<T>
) :
    Channel<T> {
    override val isClosedForReceive: Boolean
        get() = origin.isClosedForReceive

    override val isClosedForSend: Boolean
        get() = origin.isClosedForSend

    override val isEmpty: Boolean
        get() = origin.isEmpty

    override val isFull: Boolean
        get() = false

    override val onReceive: SelectClause1<T>
        get() = origin.onReceive

    override val onReceiveOrNull: SelectClause1<T?>
        get() = origin.onReceiveOrNull

    override val onSend: SelectClause2<T, SendChannel<T>>
        get() = origin.onSend

    override fun cancel() = origin.cancel()

    override fun cancel(cause: CancellationException?) = origin.cancel(cause)

    override fun cancel(cause: Throwable?): Boolean {
        cancel(cause as? CancellationException)
        return true
    }

    override fun close(cause: Throwable?): Boolean = origin.close(cause)

    override fun invokeOnClose(handler: (cause: Throwable?) -> Unit) = origin.invokeOnClose(handler)

    override fun iterator(): ChannelIterator<T> = origin.iterator()

    override fun offer(element: T): Boolean = origin.offer(element)

    override fun poll(): T? = origin.poll()

    override suspend fun receive(): T = origin.receive()

    override suspend fun receiveOrNull(): T? = origin.receiveOrNull()

    override suspend fun send(element: T) = origin.send(element)
}