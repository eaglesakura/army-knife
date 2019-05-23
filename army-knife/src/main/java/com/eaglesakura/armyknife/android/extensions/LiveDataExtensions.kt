@file:Suppress("unused")

package com.eaglesakura.armyknife.android.extensions

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.eaglesakura.armyknife.runtime.extensions.send
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

/**
 * Observe data when Lifecycle alive.
 * This method call observe always(Example, Activity/Fragment paused and more).
 * If observer should handle data every time and always, May use this method.
 *
 * e.g.)
 * fun onCreate() {
 *      exampleValue.observeAlive(this) { value ->
 *          // do something,
 *          // this message receive on pausing.
 *      }
 *      exampleValue.observe(this) { value ->
 *          // do something,
 *          // this message "NOT" receive on pausing.
 *      }
 * }
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
fun <T> LiveData<T>.observeAlive(owner: LifecycleOwner, observer: Observer<T>) {
    observeForever(observer)
    owner.lifecycle.subscribe {
        if (it == Lifecycle.Event.ON_DESTROY) {
            removeObserver(observer)
        }
    }
}

/**
 * Await receive a data in Coroutines.
 *
 * e.g.)
 * val liveData: LiveData<String> = ...
 * // await LiveData's data
 * val url = liveData.await()
 *
 * e.g.)
 * val liveData: LiveData<String> = ...
 * // await with filter
 * val url = liveData.await { it.startsWith("http") }
 */
suspend fun <T> LiveData<T>.await(filter: (value: T) -> Boolean = { true }): T {
    // check initial value.
    value?.also {
        if (filter(it)) {
            return it
        }
    }

    val channel = Channel<T>()
    val observer = Observer<T> {
        val newValue = it ?: return@Observer
        if (filter(newValue)) {
            channel.send(Dispatchers.Main, newValue)
        }
    }
    try {
        observeForever(observer)
        return channel.receive()
    } finally {
        removeObserver(observer)
    }
}