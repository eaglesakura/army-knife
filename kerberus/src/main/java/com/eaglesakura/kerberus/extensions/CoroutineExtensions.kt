package com.eaglesakura.kerberus.extensions

import com.eaglesakura.kerberus.Deferrer
import com.eaglesakura.kerberus.DelayToken
import com.eaglesakura.kerberus.Monitor
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.channels.Channel
import kotlin.coroutines.experimental.CoroutineContext

private val monitorDispatcher = newSingleThreadContext("job-interrupt")

/**
 * see CoroutineContext.monitor(Monitor)
 */
suspend fun CoroutineContext.monitor(block: suspend (job: Job) -> Unit) {
    monitor(object : Monitor {
        override suspend fun interrupt(job: Job) {
            block(job)
        }
    })
}

/**
 * "Monitor" instance will always monitoring to CoroutineContext.
 * Monitor can job cancel and more.
 *
 * This function is simple.
 * "Monitor.interrupt()" calls from job's coroutine-dispatcher with repeat.
 */
suspend fun CoroutineContext.monitor(monitor: Monitor) {
    val current = this
    val job = current[Job]!!

    val channel = Channel<Unit>()
    GlobalScope.launch(current + monitorDispatcher) {
        // initial interrupt
        monitor.interrupt(job)
        channel.send(Unit)

        // loop interrupt
        while (job.isActive) {
            withContext(current) {
                monitor.interrupt(job)
            }
            delay(Math.max(monitor.interval, 1))
        }
    }
    channel.receive()
}

/**
 * Current coroutine pause to until "Deferrer" calls to "resume()" method.
 */
suspend fun CoroutineContext.pause(callback: Deferrer) {
    val current = this
    val job = current[Job]!!
    val token = DelayTokenImpl()
    GlobalScope.launch(current + monitorDispatcher) {
        callback.onPause(job, token)
    }
    token.waitResume()
}

private class DelayTokenImpl : DelayToken {

    private val channel = Channel<Unit>()

    override suspend fun resume() {
        channel.send(Unit)
    }

    suspend fun waitResume() {
        channel.receive()
        channel.close()
    }
}