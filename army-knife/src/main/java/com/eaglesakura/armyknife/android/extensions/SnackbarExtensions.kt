package com.eaglesakura.armyknife.android.extensions

import android.widget.TextView
import androidx.annotation.ColorInt
import com.google.android.material.snackbar.Snackbar

@Suppress("NOTHING_TO_INLINE")
inline fun Snackbar.setBackgroundColor(@ColorInt color: Int) {
    view.setBackgroundColor(color)
}

@Suppress("NOTHING_TO_INLINE")
inline fun Snackbar.setTextColor(@ColorInt color: Int) {
    view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).setTextColor(color)
}