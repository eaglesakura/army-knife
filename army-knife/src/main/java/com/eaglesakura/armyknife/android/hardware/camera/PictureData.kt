package com.eaglesakura.armyknife.android.hardware.camera

import java.util.*

class PictureData(
        /**
         * 画像幅
         */
        val width: Int,
        /**
         * 画像高さ
         */
        val height: Int,
        /**
         * 撮影されたJPEGやRAWバッファ
         */
        @Suppress("MemberVisibilityCanBePrivate")
        val buffer: ByteArray) {

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
