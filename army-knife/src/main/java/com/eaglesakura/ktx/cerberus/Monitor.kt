package com.eaglesakura.ktx.cerberus

import androidx.lifecycle.Lifecycle
import com.eaglesakura.ktx.android.extensions.subscribe
import kotlinx.coroutines.experimental.Job

/**
 * Coroutine check function.
 */
interface Monitor {

    /**
     * Monitor callback interval(milli seconds)
     */
    val interval: Long
        get() = 1

    suspend fun interrupt(job: Job)
}

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
