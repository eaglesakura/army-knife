package com.eaglesakura.armyknife.android.extensions

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent


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