package com.eaglesakura.armyknife.android.extensions

import android.util.SparseArray
import androidx.core.util.valueIterator

/**
 * SparseArray to kotlin.List
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
fun <T> SparseArray<T>.toList(): List<T> {
    val result = mutableListOf<T>()

    this.valueIterator().forEach {
        result.add(it)
    }
    return result
}
