package com.eaglesakura.armyknife.sloth

import com.eaglesakura.armyknife.android.logger.Logger


@Suppress("UNUSED_PARAMETER")
object SlothLog {

    fun widget(msg: String) {
        val tag = "Sloth.Widget"
        Logger.impl.out(Logger.LEVEL_DEBUG, tag, msg)
    }

    fun bluetooth(msg: String) {
        val tag = "Sloth.Bluetooth"
        Logger.impl.out(Logger.LEVEL_DEBUG, tag, msg)
    }

    fun system(msg: String) {
        val tag = "Sloth.System"
        Logger.impl.out(Logger.LEVEL_DEBUG, tag, msg)
    }

    fun activity(msg: String) {
        val tag = "Sloth.Activity"
        Logger.impl.out(Logger.LEVEL_DEBUG, tag, msg)
    }

    fun image(msg: String) {
        val tag = "Sloth.Image"
        Logger.impl.out(Logger.LEVEL_DEBUG, tag, msg)
    }

    fun debug(msg: String) {
        val tag = "Sloth.Debug"
        Logger.impl.out(Logger.LEVEL_DEBUG, tag, msg)
    }

    fun google(msg: String) {
        val tag = "Sloth.Google"
        Logger.impl.out(Logger.LEVEL_DEBUG, tag, msg)
    }
}
