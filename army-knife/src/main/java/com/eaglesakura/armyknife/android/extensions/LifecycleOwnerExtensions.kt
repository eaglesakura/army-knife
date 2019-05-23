package com.eaglesakura.armyknife.android.extensions

import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext


/**
 * Launch with Lifecycle scope.
 *
 * e.g.)
 * fragment.launch {
 *      // do something in worker.
 * }
 */
fun LifecycleOwner.launch(context: CoroutineContext, block: CoroutineScope.() -> Unit): Job =
    lifecycle.launch(context, block)

/**
 * Launch with Lifecycle scope.
 *
 * e.g.)
 * fragment.async {
 *      // do something in worker.
 * }
 */
fun <T> LifecycleOwner.async(context: CoroutineContext, block: CoroutineScope.() -> T): Deferred<T> =
    lifecycle.async(context, block)