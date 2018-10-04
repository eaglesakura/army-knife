package com.eaglesakura.armyknife.android.extensions

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread

/**
 * Handler for UI Thread.
 */
val UIHandler = Handler(Looper.getMainLooper())

/**
 * This property is true when access by Handler thread.
 * When others thread, This property is false.
 */
val Handler.currentThread: Boolean
    get() = Thread.currentThread() == looper.thread

/**
 * If Current thread is UI thread, then returns true.
 */
val onUiThread: Boolean
    get() = Thread.currentThread() == UIHandler.looper.thread


/**
 * Call function from UI-Thread in Android Device.
 * If you call this function from the Worker-Thread, then throw Error.
 */
@UiThread
fun assertUIThread() {
    if (Thread.currentThread() != Looper.getMainLooper().thread) {
        throw Error("Thread[${Thread.currentThread()}] is not UI")
    }
}

/**
 * Call function from Worker-Thread in Android Device.
 * If you call this function from the UI-Thread, then throw Error.
 */
@WorkerThread
fun assertWorkerThread() {
    if (Thread.currentThread() == UIHandler.looper.thread) {
        throw Error("Thread[${Thread.currentThread()}] is UI")
    }
}


/**
 * When call this method in handler thread, Call "action()" soon.
 * Otherwise, post "action" object to handler thread.
 */
@Deprecated("Use to coroutines", replaceWith = ReplaceWith("GlobalScope.launch {  }"))
fun Handler.postOrRun(action: () -> Unit) {
    if (currentThread) {
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
            val handlerThread = currentThread
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
