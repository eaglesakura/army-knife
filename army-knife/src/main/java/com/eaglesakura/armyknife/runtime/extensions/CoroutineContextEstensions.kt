package com.eaglesakura.armyknife.runtime.extensions

import com.eaglesakura.armyknife.runtime.time.Duration
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

/**
 * Returns an job object from coroutines.
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
val CoroutineContext.job: Job
    get() = this[Job]!!

/**
 * Make a cancel-callback function from job.
 * Use to non-coroutine functions or in the java world.
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
fun CoroutineContext.asCancelCallback(): () -> Boolean {
    val currentJob = this.job
    // Jobの状態をチェックする.
    // Jobがない場合はキャンセルせずに実行終了を待つ.
    return { currentJob.isCancelled }
}

/**
 * Delay duration.
 */
suspend fun delay(duration: Duration) {
    kotlinx.coroutines.delay(duration.toMillis())
}