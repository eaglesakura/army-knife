package com.eaglesakura.ktx.android.extensions

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentActivity

/**
 * ActivityとActivityから探索可能なFragmentを検査し、目的のインターフェースを取得する.
 * インターフェースが複数ある場合、最初にヒットしたオブジェクトを返却する.
 */
inline fun <reified T> FragmentActivity.findInterface(): T? {
    if (this is T) {
        return this
    }

    supportFragmentManager.fragments.forEach { fragment ->
        if (fragment is T) {
            return fragment
        }
    }

    return null
}

/**
 * Force closing Input Method.
 */
fun Activity.closeIME() {
    closeIME(currentFocus ?: return)
}

/**
 * Force closing Input Method.
 */
fun Activity.closeIME(focus: View) {
    try {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(focus.windowToken, 0)
    } catch (e: Exception) {
    }
}
