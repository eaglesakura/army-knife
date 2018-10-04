package com.eaglesakura.armyknife.android.logger

@Deprecated("replace to Timber or such libraries.")
class ConsoleLogger : Logger.Impl {

    override fun out(level: Int, tag: String, msg: String) {
        val trace = Exception().stackTrace
        val elem = trace[Math.min(trace.size - 1, 2)]

        val message = "$tag | ${elem.fileName}[${elem.lineNumber}] : $msg"
        when (level) {
            Logger.LEVEL_ERROR -> System.err.println(message)
            else -> System.out.println(message)
        }
    }
}