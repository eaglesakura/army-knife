package com.eaglesakura.armyknife.android.extensions

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper

/**
 * Handler for UI Thread.
 */
val UIHandler = Handler(Looper.getMainLooper())

/**
 * This property is true when access by Handler thread.
 * When others thread, This property is false.
 */
val Handler.isCurrentThread: Boolean
    get() = Thread.currentThread() == looper.thread

/**
 * When call this method in handler thread, Call "action()" soon.
 * Otherwise, post "action" object to handler thread.
 */
fun Handler.postOrRun(action: () -> Unit) {
    if (isCurrentThread) {
        action()
    } else {
        post(action)
    }
}

/**
 * Handler for async looper.
 */
class AsyncHandler(private val thread: HandlerThread) : Handler(thread.looper) {
    fun dispose() {
        try {
            val handlerThread = isCurrentThread
            thread.quit()
            if (!handlerThread) {
                thread.join()
            }
        } catch (e: Exception) {
        }
    }

    companion object {
        fun newInstance(name: String): AsyncHandler {
            val thread = HandlerThread(name)
            thread.start()
            return AsyncHandler(thread)
        }
    }
}
