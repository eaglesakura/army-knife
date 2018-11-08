package com.eaglesakura.kerberus

import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext


/**
 * Launch coroutine with semaphore.
 *
 * e.g.)
 * GlobalScope.launch(Dispatchers.Main, 1, TimeUnit.SECONDS) {
 *      // do in coroutines.
 * }
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
fun CoroutineScope.launch(
    context: CoroutineContext,
    duration: Long,
    timeUnit: TimeUnit,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return GlobalScope.launch(context) {
        withTimeout(timeUnit.toMillis(duration)) {
            block()
        }
    }
}

/**
 * Async coroutine with semaphore.
 *
 * e.g.)
 * GlobalScope.async(Dispatchers.Main, 1, TimeUnit.SECONDS) {
 *      // do in coroutines.
 * }
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
fun <T> CoroutineScope.async(
    context: CoroutineContext,
    duration: Long,
    timeUnit: TimeUnit,
    block: suspend CoroutineScope.() -> T
): Deferred<T> {
    return GlobalScope.async(context) {
        withTimeout(timeUnit.toMillis(duration)) {
            block()
        }
    }
}

/**
 * Run with semaphore, include withTimeout.
 *
 * e.g.)
 * withSemaphore(Semaphore.Network, 1, TimeUnit.SECONDS) {
 *      // do something
 * }
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
suspend fun <T> withSemaphore(
    semaphore: Semaphore,
    duration: Long,
    timeUnit: TimeUnit,
    block: suspend () -> T
): T {
    return withTimeout(timeUnit.toMillis(duration)) {
        semaphore.run(block)
    }
}
