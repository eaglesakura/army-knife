package com.eaglesakura.armyknife.timber

import android.util.Log
import timber.log.Timber

/**
 * Logging with StackTrace.
 *
 * e.g.)
 * init(context: Context) {
 *      Timber.plant(StackTraceTree())
 * }
 *
 * fun Any.console(msg: String) {
 *      Timber.d(msg)
 * }
 */
class StackTraceTree(
        /**
         * Back trace depth num.
         * Default is [StackTraceTree][Timber][Console wrapper method] = 6 stack will popping.
         */
        private val popStack: Int = 6,

        /**
         * Console output function.
         * Default is Log.d() in Android SDK.
         */
        private val console: (tag: String, message: String) -> Unit = { tag, message -> Log.d(tag, message) }
) : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {

        val outTag = tag ?: javaClass.simpleName
        val outMessage = Throwable().let {
            val trace = Exception().stackTrace
            val elem = trace[Math.min(trace.size - 1, popStack)]
            "${elem.fileName}[${elem.lineNumber}] : $message"
        }

        console(outTag, outMessage)
    }
}