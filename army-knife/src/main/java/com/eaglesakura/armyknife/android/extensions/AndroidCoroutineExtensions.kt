package com.eaglesakura.armyknife.android.extensions

import com.eaglesakura.armyknife.android.ApplicationRuntime
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.launch
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