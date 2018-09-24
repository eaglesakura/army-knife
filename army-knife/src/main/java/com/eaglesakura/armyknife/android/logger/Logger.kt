package com.eaglesakura.armyknife.android.logger

import android.content.Context
import com.eaglesakura.armyknife.android.ApplicationRuntime
import com.eaglesakura.armyknife.android.extensions.debugMode

/**
 * Print log to console.
 * When UnitTest from robolectric, It use printf.
 * When Android device, It use Log.{d,i,e}.
 */
object Logger {
    var impl: Impl = newImpl(false)
        private set

    const val LEVEL_INFO = 1
    const val LEVEL_DEBUG = 2
    const val LEVEL_ERROR = 3

    interface Impl {
        /**
         * Console output.
         */
        fun out(level: Int, tag: String, msg: String)
    }

    private fun newImpl(printStack: Boolean): Impl =
            if (ApplicationRuntime.ROBOLECTRIC) {
                ConsoleLogger()
            } else {
                AndroidLogger().also {
                    it.stackInfo = printStack
                }
            }

    /**
     * if context is debuggable,
     * Logging with FileName and line number to console.
     */
    fun init(context: Context) {
        impl = newImpl(context.debugMode)
    }

    fun error(tag: String, msg: String): String {
        impl.out(LEVEL_ERROR, tag, msg)
        return msg
    }

    fun debug(tag: String, msg: String): String {
        impl.out(LEVEL_DEBUG, tag, msg)
        return msg
    }

    fun info(tag: String, msg: String): String {
        impl.out(LEVEL_INFO, tag, msg)
        return msg
    }
}