package com.eaglesakura.armyknife.runtime.time

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

/**
 * Current time object.
 * This class has been always constant, not modifiable.
 */
@Suppress("unused")
@Parcelize
class Clock(private val milliSeconds: Long) : Parcelable {

    /**
     * Returns raw value as milliseconds.
     */
    fun toMillis(): Long = milliSeconds

    /**
     * Returns raw value as Date.
     */
    fun toDate(): Date = Date(milliSeconds)

    operator fun plus(duration: Duration): Clock {
        return Clock(milliSeconds + duration.toMillis())
    }

    operator fun minus(clock: Clock): Duration {
        return Duration(milliSeconds - clock.milliSeconds)
    }

    operator fun minus(duration: Duration): Clock {
        return Clock(milliSeconds - duration.toMillis())
    }

    override fun toString(): String {
        return "Clock(milliSeconds=$milliSeconds)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Clock

        if (milliSeconds != other.milliSeconds) return false

        return true
    }

    override fun hashCode(): Int {
        return milliSeconds.hashCode()
    }
}