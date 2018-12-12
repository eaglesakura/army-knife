@file:Suppress("unused")

package com.eaglesakura.armyknife.android.extensions

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread

/**
 * Handler for UI Thread.
 *
 * e.g.)
 * UIHandler.post {
 *      // do something on UI thread.
 * }
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
val UIHandler = Handler(Looper.getMainLooper())

/**
 * This property is true when access by Handler thread.
 * When others thread, This property is false.
 */
@Deprecated("rename to currentIsHandlerThread", ReplaceWith("currentIsHandlerThread"))
val Handler.currentThread: Boolean
    get() = Thread.currentThread() == looper.thread

/**
 * This property is true when access by Handler thread.
 * When others thread, This property is false.
 */
val Handler.currentIsHandlerThread: Boolean
    get() = Thread.currentThread() == looper.thread

/**
 * If Current thread is UI thread, then returns true.
 *
 * e.g.)
 * if(onUiThread) {
 *      // do something on UI thread.
 * }
 */
val onUiThread: Boolean
    get() = Thread.currentThread() == UIHandler.looper.thread


/**
 * robolectric runtime is true.
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
private val robolectric: Boolean = try {
    Class.forName("org.robolectric.Robolectric")
    true
} catch (err: ClassNotFoundException) {
    false
}

/**
 * Call function from UI-Thread in Android Device.
 * If you call this function from the Worker-Thread, then throw Error.
 *
 * e.g.)
 * @UiTHread
 * fun onClick() {
 *      assertUIThread()    // throw error on worker thread.
 * }
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
@UiThread
fun assertUIThread() {
    if (robolectric) {
        return
    }

    if (Thread.currentThread() != Looper.getMainLooper().thread) {
        throw Error("Thread[${Thread.currentThread()}] is not UI")
    }
}

/**
 * Call function from Worker-Thread in Android Device.
 * If you call this function from the UI-Thread, then throw Error.
 *
 * e.g.)
 * @WorkerThread
 * fun httpFetch() {
 *      assertWorkerThread()    // throw error on ui thread.
 * }
 */
@WorkerThread
fun assertWorkerThread() {
    if (robolectric) {
        return
    }

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
    if (currentIsHandlerThread) {
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
            val handlerThread = currentIsHandlerThread
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
