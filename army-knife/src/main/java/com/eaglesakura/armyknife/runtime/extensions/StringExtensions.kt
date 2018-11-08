package com.eaglesakura.armyknife.runtime.extensions

import com.eaglesakura.armyknife.runtime.Base64Impl

/**
 * Base64 encoded string to byte array.
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
fun String.decodeBase64(): ByteArray = Base64Impl.stringToByteArray(this)