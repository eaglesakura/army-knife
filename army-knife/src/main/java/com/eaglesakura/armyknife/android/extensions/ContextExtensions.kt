package com.eaglesakura.armyknife.android.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat

/**
 * Load drawable resource with set tint color to it.
 */
fun Context.getDrawableCompat(@DrawableRes resId: Int, @ColorInt tint: Int = 0): Drawable {
    val result = ResourcesCompat.getDrawable(resources, resId, theme)!!
    if (tint != 0) {
        DrawableCompat.setTint(result, tint)
    }
    return result
}