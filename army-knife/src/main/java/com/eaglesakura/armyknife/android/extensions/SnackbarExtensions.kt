package com.eaglesakura.armyknife.android.extensions

import android.widget.TextView
import androidx.annotation.ColorInt
import com.google.android.material.snackbar.Snackbar

/**
 * set background color in Snackbar.
 *
 * e.g.)
 * val snackbar: Snackbar
 * val color: Int = Colors.RED
 * snackbar.setBackgroundColor(color)
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Snackbar.setBackgroundColor(@ColorInt color: Int) {
    view.setBackgroundColor(color)
}

/**
 * set text color in Snackbar.
 *
 * e.g.)
 * val snackbar: Snackbar
 * val color: Int = Colors.RED
 * snackbar.setTextColor(color)
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Snackbar.setTextColor(@ColorInt color: Int) {
    view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).setTextColor(color)
}