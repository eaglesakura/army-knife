package com.eaglesakura.kerberus

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.Job

/**
 * Coroutine check function.
 */
@Deprecated("Do not use this. This class is a junk.")
interface Monitor {

    /**
     * Monitor callback interval(milli seconds)
     */
    val interval: Long
        get() = 1

    suspend fun interrupt(job: Job)
}

@Deprecated("Do not use this. This class is a junk.")
class CancelOnBackgroundMonitor(lifecycle: Lifecycle) : Monitor {
    override val interval: Long
        get() = 1

    private var job: Job? = null

    private var pauseCount: Int = 0

    init {
        lifecycle.subscribe {
            if (it == Lifecycle.Event.ON_PAUSE) {
                ++pauseCount
                job?.cancel()
            }
        }
    }

    override suspend fun interrupt(job: Job) {
        if (this.job == null) {
            this.job = job
        }

        if (pauseCount > 0) {
            job.cancel()
        }
    }
}

@Deprecated("Do not use this. This class is a junk.")
class CancelOnDestroyMonitor(private val lifecycle: Lifecycle) : Monitor {
    override val interval: Long
        get() = 1

    private var job: Job? = null

    init {
        lifecycle.subscribe {
            if (it == Lifecycle.Event.ON_DESTROY) {
                job?.cancel()
            }
        }
    }

    override suspend fun interrupt(job: Job) {
        if (this.job == null) {
            this.job = job
        }

        if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
            job.cancel()
        }
    }
}

/**
 * Subscribe lifecycle's event.
 */
private fun Lifecycle.subscribe(receiver: (event: Lifecycle.Event) -> Unit) {
    this.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
        fun onAny(@Suppress("UNUSED_PARAMETER") source: LifecycleOwner, event: Lifecycle.Event) {
            receiver.invoke(event)
        }
    })
}

