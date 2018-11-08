package com.eaglesakura.armyknife.android.extensions

import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


/**
 * Run suspend-block in Android UI thread.
 * runBlocking of coroutine-runtime is not support in Android UI Thread.
 * When uses coroutines version 0.24.x then use this.
 */
@Deprecated(message = "Revert specifications in 0.25.3, use runBlocking{}")
fun <T> runBlockingInUI(context: CoroutineContext = Dispatchers.Default, block: suspend CoroutineScope.() -> T): T {
    assertUIThread()
    if (context == Dispatchers.Main) {
        throw IllegalArgumentException("UI context has been NOT supported.")
    }

    var values: Pair<T?, Exception?>? = null
    GlobalScope.launch(context) {
        values = try {
            Pair<T?, Exception?>(block(this), null)
        } catch (e: Exception) {
            Pair<T?, Exception?>(null, e)
        }
    }

    while (values == null) {
        Thread.sleep(1)
    }

    if (values?.second != null) {
        throw values!!.second!!
    } else {
        @Suppress("UNCHECKED_CAST")
        return values!!.first as T
    }
}

/**
 * Link to lifecycle.
 * When destroyed a lifecycle object then cancel coroutine.
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
fun CoroutineContext.with(lifecycle: Lifecycle) {
    val context = this
    lifecycle.subscribeWithCancel { event, cancel ->
        if (!context.isActive) {
            cancel()
            return@subscribeWithCancel
        }
        if (event == Lifecycle.Event.ON_DESTROY) {
            context.cancel()
            cancel()
        }
    }
}