@file:Suppress("unused")

package com.eaglesakura.armyknife.android.extensions

import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job

/**
 * When lifecycle on destroyed, then cancel job.
 */
fun Job.with(lifecycle: Lifecycle) {
    val job = this
    lifecycle.subscribeWithCancel { event, cancel ->
        if (!isActive) {
            cancel()
            return@subscribeWithCancel
        }

        if (event == Lifecycle.Event.ON_DESTROY) {
            job.cancel(CancellationException("Cancel on Lifecycle destroyed"))
            cancel()
        }
    }
}

/**
 * When lifecycle on background, then cancel job.
 */
fun Job.withForeground(lifecycle: Lifecycle) {
    val job = this
    lifecycle.subscribeWithCancel { event, cancel ->
        if (!isActive) {
            cancel()
            return@subscribeWithCancel
        }

        @Suppress("NON_EXHAUSTIVE_WHEN")
        when (event) {
            Lifecycle.Event.ON_PAUSE, Lifecycle.Event.ON_STOP, Lifecycle.Event.ON_DESTROY -> {
                job.cancel(CancellationException("Cancel on Lifecycle destroyed"))
                cancel()
            }
        }
    }
}