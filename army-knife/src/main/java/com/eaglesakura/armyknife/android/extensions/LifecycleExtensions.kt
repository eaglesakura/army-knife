package com.eaglesakura.armyknife.android.extensions

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.experimental.CancellationException
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.android.Main
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext


/**
 * Subscribe lifecycle's event.
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
 */
suspend fun delay(lifecycle: Lifecycle, targetEvent: Lifecycle.Event) {
    withContext(Dispatchers.Main) {
        if (lifecycle.currentState == targetEvent) {
            return@withContext
        }

        val channel = Channel<Lifecycle.Event>()
        lifecycle.subscribeWithCancel { event, cancel ->
            if (event == targetEvent) {
                // resume coroutine
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
