package com.eaglesakura.armyknife.timber

import android.util.Log
import timber.log.Timber

/**
 * Simple console(println) out tree.
 */
class ConsoleTree(
        /**
         * Console output function.
         * Default is Log.d() in Android SDK.
         */
        private val console: (tag: String, message: String) -> Unit = { tag, message -> Log.d(tag, message) }
) : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        console(tag ?: javaClass.simpleName, message)
    }
}