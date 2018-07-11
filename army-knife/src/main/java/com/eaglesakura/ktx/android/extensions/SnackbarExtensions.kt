package com.eaglesakura.ktx.android.extensions

import android.widget.TextView
import androidx.annotation.ColorInt
import com.eaglesakura.ktx.R
import com.google.android.material.snackbar.Snackbar

@Suppress("NOTHING_TO_INLINE")
inline fun Snackbar.setBackgroundColor(@ColorInt color: Int) {
    view.setBackgroundColor(color)
}

@Suppress("NOTHING_TO_INLINE")
inline fun Snackbar.setTextColor(@ColorInt color: Int) {
    view.findViewById<TextView>(R.id.snackbar_text).setTextColor(color)
}