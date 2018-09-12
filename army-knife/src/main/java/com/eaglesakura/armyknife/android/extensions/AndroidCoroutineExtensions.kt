package com.eaglesakura.armyknife.android.extensions

import androidx.lifecycle.Lifecycle
import com.eaglesakura.armyknife.android.ApplicationRuntime
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import kotlin.coroutines.experimental.CoroutineContext


/**
 * Run suspend-block in Android UI thread.
 * runBlocking of coroutine-runtime is not support in Android UI Thread.
 * When uses coroutines version 0.24.x then use this.
 */
fun <T> runBlockingInUI(context: CoroutineContext = CommonPool, block: suspend CoroutineScope.() -> T): T {
    ApplicationRuntime.assertUIThread()
    if (context == UI) {
        throw IllegalArgumentException("UI context has been NOT supported.")
    }

    var values: Pair<T?, Exception?>? = null
    launch(context) {
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
 */
fun CoroutineContext.with(lifecycle: Lifecycle) {
    var context: CoroutineContext? = this

    lifecycle.subscribe { event ->
        val ctx = context ?: return@subscribe

        if (event == Lifecycle.Event.ON_DESTROY) {
            if (ctx.isActive) {
                ctx.cancel(CancellationException("Lifecycle[$lifecycle] was destroyed."))
            }
            context = null
        }
    }
}