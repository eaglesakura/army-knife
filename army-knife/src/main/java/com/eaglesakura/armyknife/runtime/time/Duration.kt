package com.eaglesakura.armyknife.runtime.time

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.concurrent.TimeUnit


/**
 * between duration of time to time.
 */
@Suppress("MemberVisibilityCanBePrivate")
@Parcelize
data class Duration internal constructor(private val milliSeconds: Long) : Parcelable {

    /**
     * Returns raw value to milliseconds.
     */
    fun toMillis(): Long = milliSeconds

    /**
     * Returns raw value to seconds.
     */
    fun toSeconds(): Double = milliSeconds.toDouble() / 1000.0

    /**
     * Returns raw value to Minutes.
     */
    fun toMinutes(): Double = toSeconds() / 60.0

    /**
     * Returns raw value to hours.
     */
    fun toHours(): Double = toSeconds() / 3600.0

    /**
     * Returns raw value to days.
     */
    fun toDays(): Double = toHours() / 24.0

    companion object {
        /**
         * Duration from TimeUnit.
         */
        fun from(time: Long, unit: TimeUnit): Duration = Duration(unit.toMillis(time))
    }
}