package com.eaglesakura.armyknife.android.extensions

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel


/**
 * Subscribe lifecycle's event.
 *
 * e.g.)
 * fragment.lifecycle.subscribe { event ->
 *      if(event == Lifecycle.Event.ON_RESUME) {
 *          // do something.
 *      }
 * }
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
fun Lifecycle.subscribe(receiver: (event: Lifecycle.Event) -> Unit) {
    this.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
        fun onAny(@Suppress("UNUSED_PARAMETER") source: LifecycleOwner, event: Lifecycle.Event) {
            receiver(event)
        }
    })
}

/**
 * Subscribe event with cancel callback.
 * If you should be ignore receiver, call "cancel()" function.
 *
 * e.g.)
 * fragment.lifecycle.subscribe { event, cancel ->
 *      if(event == Lifecycle.Event.ON_RESUME) {
 *          // do something.
 *
 *          cancel() // cancel subscribe events.
 *      }
 * }
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
fun Lifecycle.subscribeWithCancel(receiver: (event: Lifecycle.Event, cancel: () -> Unit) -> Unit) {
    val self = this
    self.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
        fun onAny(@Suppress("UNUSED_PARAMETER") source: LifecycleOwner, event: Lifecycle.Event) {
            @Suppress("MoveLambdaOutsideParentheses")
            receiver(event, { self.removeObserver(this) })
        }
    })
}

/**
 * Suspend current coroutines context until receive lifecycle event.
 *
 * e.g.)
 * suspend fun awaitToResume() {
 *      delay(fragment.lifecycle, Lifecycle.Event.ON_RESUME)
 *
 *      // do something, fragment on resumed.
 * }
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
suspend fun delay(lifecycle: Lifecycle, targetEvent: Lifecycle.Event) {
    withContext(Dispatchers.Main) {
        yield()
        if (lifecycle.currentState == targetEvent) {
            return@withContext
        }

        val channel = Channel<Lifecycle.Event>()
        lifecycle.subscribeWithCancel { event, cancel ->
            if (event == targetEvent) {
                // resume coroutines
                launch(Dispatchers.Main) {
                    channel.send(event)
                }
                cancel()
                return@subscribeWithCancel
            }

            if (event == Lifecycle.Event.ON_DESTROY) {
                // do not receive!!
                channel.cancel(CancellationException("Lifecycle was deleted, do not resume."))
            }
        }
        channel.receive()
    }
}
