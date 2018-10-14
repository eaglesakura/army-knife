package com.eaglesakura.armyknife.runtime.extensions

import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

/**
 * CoroutineContextから現在のJobを取得する.
 */
val CoroutineContext.job: Job?
    get() = this[Job]

/**
 * Make a cancel-callback function from job.
 * Use to non-coroutine functions or in the java world.
 */
fun CoroutineContext.asCancelCallback(): () -> Boolean {
    val currentJob = this.job
    // Jobの状態をチェックする.
    // Jobがない場合はキャンセルせずに実行終了を待つ.
    return { currentJob?.isCancelled ?: false }
}