package com.eaglesakura.armyknife.runtime.extensions

import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.withContext
import java.util.concurrent.TimeUnit
import kotlin.coroutines.experimental.CoroutineContext
import kotlin.coroutines.experimental.coroutineContext


/**
 * Timeout with `current` coroutine context.
 */
suspend fun <T> withTimeout(current: CoroutineContext, time: Long, unit: TimeUnit, block: suspend CoroutineScope.() -> T): T {
    return kotlinx.coroutines.experimental.withTimeout(time, unit) {
        withContext(current + coroutineContext) {
            block()
        }
    }
}