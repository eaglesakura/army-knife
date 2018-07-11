package com.eaglesakura.ktx.android.logger

import android.util.Log


class AndroidLogger : Logger.Impl {
    var stackInfo: Boolean = false

    override fun out(level: Int, tag: String, msg: String) {
        var message = msg
        if (stackInfo) {
            val trace = Exception().stackTrace
            val elem = trace[Math.min(trace.size - 1, 2)]
            message = String.format("%s[%d] : %s", elem.fileName, elem.lineNumber, msg)
        }
        when (level) {
            Logger.LEVEL_INFO -> Log.i(tag, message)
            Logger.LEVEL_ERROR -> Log.e(tag, message)
            else -> Log.d(tag, message)
        }
    }
}