@file:Suppress("unused")

package com.eaglesakura.armyknife.android.extensions

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentActivity

/**
 * Find interface by Activity and children.
 * If it has many interfaces then returns 1st hit object.
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
inline fun <reified T> FragmentActivity.findInterface(): T? {
    if (this is T) {
        return this
    }

    supportFragmentManager.fragments.forEach { fragment ->
        fragment.findInterface<T>()?.also {
            return it
        }
    }

    return null
}

/**
 * Force closing Input Method.
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
fun Activity.closeIME() {
    closeIME(currentFocus ?: return)
}

/**
 * Force closing Input Method.
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
fun Activity.closeIME(focus: View) {
    try {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            focus.windowToken,
            0
        )
    } catch (e: Exception) {
    }
}
