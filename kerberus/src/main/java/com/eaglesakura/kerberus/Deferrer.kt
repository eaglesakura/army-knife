package com.eaglesakura.kerberus

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

interface DelayToken {
    suspend fun resume()
}

interface Deferrer {
    /**
     * Should call "delay()" or spin-lock in this method.
     * Job is pausing to until call "resume()" method.
     */
    suspend fun onPause(job: Job, token: DelayToken)
}

/**
 * Stop this task, wait for foreground.
 */
class DeferredOnForeground(private val lifecycle: Lifecycle) : Deferrer {

    /**
     * Delay for App to Foreground event.
     * suspend-method is stop when background.
     */
    override suspend fun onPause(job: Job, token: DelayToken) {
        if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
            job.cancel()
            return
        }

        lifecycle.runOnForeground {
            launch(UI) { token.resume() }
        }
    }
}


/**
 * Foregroundになったタイミングでactionを実行する.
 */
internal fun Lifecycle.runOnForeground(action: () -> Unit) {
    if (currentState == Lifecycle.State.DESTROYED) {
        return
    }

    if (currentState == Lifecycle.State.RESUMED) {
        launch(UI) { action() }
        return
    } else {
        this.addObserver(object : LifecycleObserver {
            @Suppress("unused")
            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun onResume() {
                launch(UI) { action() }
                removeObserver(this)
            }
        })
    }
}
