package com.eaglesakura.kerberus

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Deprecated("Do not use this. This class is a junk.")
interface DelayToken {
    suspend fun resume()
}

@Deprecated("Do not use this. This class is a junk.")
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
@Deprecated("Do not use this. This class is a junk.")
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
            GlobalScope.launch(Dispatchers.Main) { token.resume() }
        }
    }
}


/**
 * Foregroundになったタイミングでactionを実行する.
 */
@Deprecated("Do not use this. This class is a junk.")
internal fun Lifecycle.runOnForeground(action: () -> Unit) {
    if (currentState == Lifecycle.State.DESTROYED) {
        return
    }

    if (currentState == Lifecycle.State.RESUMED) {
        GlobalScope.launch(Dispatchers.Main) { action() }
        return
    } else {
        this.addObserver(object : LifecycleObserver {
            @Suppress("unused")
            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun onResume() {
                GlobalScope.launch(Dispatchers.Main) { action() }
                removeObserver(this)
            }
        })
    }
}
