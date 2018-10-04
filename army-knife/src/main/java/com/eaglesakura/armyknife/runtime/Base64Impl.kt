package com.eaglesakura.armyknife.runtime

import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi

internal object Base64Impl {

    val byteArrayToString: (array: ByteArray) -> String

    val stringToByteArray: (str: String) -> ByteArray

    private const val ANDROID_URL_SAFE = 8

    private const val ANDROID_NO_WRAP = 2

    private const val ANDROID_NO_PADDING = 1

    private const val ANDROID_NO_CLOSE = 16

    private const val ANDROID_FLAG = ANDROID_NO_CLOSE or ANDROID_NO_PADDING or ANDROID_NO_WRAP or ANDROID_URL_SAFE

    init {
        val runOnAndroid = try {
            Class.forName("android.util.Base64")
            true
        } catch (err: ClassNotFoundException) {
            false
        }

        if (runOnAndroid) {
            // for Android
            byteArrayToString = { bytArray -> Base64.encodeToString(bytArray, ANDROID_FLAG) }
            stringToByteArray = { str -> Base64.decode(str, ANDROID_FLAG) }
        } else {
            // for JVM
            @RequiresApi(Build.VERSION_CODES.O)
            byteArrayToString = { byteArray -> java.util.Base64.getEncoder().encodeToString(byteArray) }
            @RequiresApi(Build.VERSION_CODES.O)
            stringToByteArray = { str -> java.util.Base64.getDecoder().decode(str) }
        }
    }
}