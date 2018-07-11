package com.eaglesakura.ktx.runtime.extensions

import com.eaglesakura.ktx.runtime.Base64Impl

/**
 * Base64 encoded string to byte array.
 */
fun String.decodeBase64(): ByteArray = Base64Impl.stringToByteArray(this)