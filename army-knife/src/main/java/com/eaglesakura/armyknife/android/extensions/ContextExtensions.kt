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

/**
 * string xmlリソース名から文字列リソースを取得する
 */
fun Context.getStringFromIdName(resName: String): String? {
    return try {
        val id = resources.getIdentifier(
                resName,
                "string",
                packageName
        )

        resources.getString(id)
    } catch (e: Exception) {
        null
    }

}

/**
 * string xmlリソース名から文字列リソースを取得する
 */
fun Context.getStringFromIdName(resName: String, vararg arg: Any): String? {
    return try {
        val id = resources.getIdentifier(
                resName,
                "string",
                packageName
        )

        resources.getString(id, *arg)
    } catch (e: Exception) {
        null
    }

}

/**
 * string xmlリソース名から文字列リソースを取得する
 */
fun Context.getIntegerFromIdName(resName: String): Int? {
    return try {
        val id = resources.getIdentifier(
                resName,
                "integer",
                packageName
        )
        resources.getInteger(id)
    } catch (e: Exception) {
        null
    }

}
