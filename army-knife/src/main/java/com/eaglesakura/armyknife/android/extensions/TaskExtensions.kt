package com.eaglesakura.armyknife.android.extensions

import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Result
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

/**
 * await a Google Play Services task until complete or cancellation in Coroutines.
 *
 * e.g.)
 * val task: Task<Result> = ...
 * task.awaitWithSuspend()
 */
@Deprecated("rename to awaitInCoroutines", ReplaceWith("awaitInCoroutines"))
suspend fun <T> Task<T>.awaitWithSuspend(): Task<T> = this.awaitInCoroutines()

/**
 * await a Google Play Service's task until complete or cancellation in Coroutines.
 *
 * e.g.)
 * val task: Task<Result> = ...
 * task.awaitInCoroutines()
 */
suspend fun <T> Task<T>.awaitInCoroutines(): Task<T> {
    val channel = Channel<Unit>()
    addOnCompleteListener {
        GlobalScope.launch(Dispatchers.Main) {
            channel.send(Unit)
        }
    }
    addOnCanceledListener {
        channel.close(CancellationException())
    }
    channel.receive()
    return this
}

/**
 * await a Google Play Service's task until complete or cancellation in Coroutines.
 * When coroutines job canceled, then cancel this task.
 *
 * e.g.)
 * val task: PendingResult<Result> = ...
 * task.awaitWithSuspend()
 */
@Deprecated("rename to awaitInCoroutines", ReplaceWith("awaitInCoroutines"))
suspend fun <T : Result> PendingResult<T>.awaitWithSuspend(): T = this.awaitInCoroutines()

/**
 * await a Google Play Service's task until complete or cancellation in Coroutines.
 * When coroutines job canceled, then cancel this task.
 *
 * e.g.)
 * val task: PendingResult<Result> = ...
 * task.awaitInCoroutines()
 */
suspend fun <T : Result> PendingResult<T>.awaitInCoroutines(): T {
    val channel = Channel<T>()
    this.setResultCallback {
        GlobalScope.launch(Dispatchers.Main) { channel.send(it) }
    }
    try {
        return channel.receive()
    } catch (e: CancellationException) {
        this.cancel()
        throw e
    }
}
