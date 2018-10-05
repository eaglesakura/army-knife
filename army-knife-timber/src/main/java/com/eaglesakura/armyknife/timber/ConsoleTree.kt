package com.eaglesakura.armyknife.timber

import android.util.Log
import timber.log.Timber

/**
 * Simple console(println) out tree.
 */
class ConsoleTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        Log.println(priority, tag, message)
    }
}