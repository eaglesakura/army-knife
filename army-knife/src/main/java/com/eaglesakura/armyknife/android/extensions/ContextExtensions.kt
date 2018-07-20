package com.eaglesakura.armyknife.android.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.Surface
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import android.view.Surface.ROTATION_270
import android.view.Surface.ROTATION_180
import android.view.Surface.ROTATION_90
import android.view.Surface.ROTATION_0
import android.view.WindowManager


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


/**
 *
 */
fun Context.getDeviceRotateDegree(): Int {
    val surfaceRotation = (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.rotation
    when (surfaceRotation) {
        Surface.ROTATION_0 -> return 0
        Surface.ROTATION_90 -> return 90
        Surface.ROTATION_180 -> return 180
        Surface.ROTATION_270 -> return 270
    }
    return 0
}