package com.eaglesakura.firearm.app.channel

import androidx.annotation.CheckResult
import androidx.annotation.UiThread
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.eaglesakura.armyknife.android.extensions.subscribe
import com.eaglesakura.armyknife.runtime.coroutines.DelegateChannel
import kotlinx.coroutines.channels.Channel
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Channel holder.
 *
 * CAUTION!!
 * This object can't save to Bundle.
 * ChannelRegistry use to  an activity with short time or check to the runtime permission.
 * DON'T USE LONG TIME ACTIVITY.
 */
@Deprecated("Replace to firearm.channel.ChannelRegistry")
class ChannelRegistry(private val owner: LifecycleOwner) {
    /**
     * Did register channels.
     *
     */
    private val channels = mutableMapOf<Any, Channel<*>>()

    private val lock = ReentrantLock()

    init {
        owner.lifecycle.subscribe {
            if (it == Lifecycle.Event.ON_DESTROY) {
                onDestroy()
            }
        }
    }

    /**
     * Returns channel num.
     */
    val size: Int
        get() = lock.withLock {
            channels.size
        }

    internal fun unregister(key: Any) {
        lock.withLock {
            channels.remove(key)
        }
    }

    /**
     * Get channel from key.
     * If channel not found then throws exception from function.
     */
    fun <T> get(key: Any): Channel<T> {
        return lock.withLock {
            @Suppress("UNCHECKED_CAST")
            channels[key]!! as Channel<T>
        }
    }

    /**
     * Add channel to registry.
     * This function returns new channel for using, You should "close()" it.
     */
    @CheckResult
    fun <T> register(key: Any, channel: Channel<T>): Channel<T> {
        lock.withLock {
            val old = channels[key]
            if (old != null) {
                throw IllegalArgumentException("Registry contains key[$key]")
            }

            val result = RegisteredChannel(key, channel, this)
            channels[key] = result
            return result
        }
    }

    @UiThread
    private fun onDestroy() {
        lock.withLock {
            for (item in channels) {
                try {
                    item.value.close()
                } catch (err: Exception) {
                }
            }

            channels.clear()
        }
    }

    internal class RegisteredChannel<T>(
            private val key: Any,
            origin: Channel<T>,
            private val registry: ChannelRegistry
    ) : DelegateChannel<T>(origin) {

        override fun cancel(): Boolean {
            registry.unregister(key)
            return super.cancel()
        }

        override fun cancel(cause: Throwable?): Boolean {
            registry.unregister(key)
            return super.cancel(cause)
        }

        override fun close(cause: Throwable?): Boolean {
            registry.unregister(key)
            return super.close(cause)
        }
    }
}

