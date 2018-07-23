package com.eaglesakura.armyknife.android.hardware.camera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.util.*

data class PictureData(
        val width: Int,
        val height: Int,
        /**
         * This buffer was generated from Camera API.
         * The Buffer formats are "Jpeg" or "DNG".
         */
        @Suppress("MemberVisibilityCanBePrivate")
        val buffer: ByteArray) {

    fun decodeImage(): Bitmap {
        return BitmapFactory.decodeByteArray(buffer, 0, buffer.size)!!
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PictureData

        if (width != other.width) return false
        if (height != other.height) return false
        if (!Arrays.equals(buffer, other.buffer)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = width
        result = 31 * result + height
        result = 31 * result + Arrays.hashCode(buffer)
        return result
    }
}
