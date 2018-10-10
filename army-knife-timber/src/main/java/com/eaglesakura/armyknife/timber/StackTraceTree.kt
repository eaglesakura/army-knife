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
        private val popStack: Int = POP_STACK_DEFAULT
) : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val outTag = tag ?: javaClass.simpleName
        val outMessage = Throwable().let {
            val trace = Exception().stackTrace
            val elem = trace[Math.min(trace.size - 1, popStack)]
            "${elem.fileName}[${elem.lineNumber}] : $message"
        }

        Log.println(priority, outTag, outMessage)
    }

    companion object {
        /**
         * Back trace depth num.
         * Default is [StackTraceTree][Timber] = 5 stack will popping.
         */
        const val POP_STACK_DEFAULT = 5
    }
}