package com.eaglesakura.armyknife.android.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.os.Build
import android.provider.Settings
import android.view.Surface
import android.view.WindowManager
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

val Context.deviceRotateDegree: Int
    get() {
        val surfaceRotation = (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.rotation
        when (surfaceRotation) {
            Surface.ROTATION_0 -> return 0
            Surface.ROTATION_90 -> return 90
            Surface.ROTATION_180 -> return 180
            Surface.ROTATION_270 -> return 270
        }
        return 0
    }

/**
 * If this app debugging now,
 * This property returns true,
 */
val Context.debugMode: Boolean
    get() = packageManager.getApplicationInfo(packageName, 0)?.let { appInfo ->
        return appInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE == ApplicationInfo.FLAG_DEBUGGABLE
    } ?: false

/**
 * When developer mode enabled on this device, This property return true.
 * But, API Level less than 17, This property always returns false.
 */
val Context.devloperModeDevice: Boolean
    get() = if (Build.VERSION.SDK_INT < 17) {
        false
    } else {
        Settings.Secure.getInt(contentResolver, Settings.Global.ADB_ENABLED, 0) != 0
    }

/**
 * This method returns true when android-device connected to network.
 */
@SuppressLint("MissingPermission")
fun Context.isConnectedNetwork(): Boolean {
    val service = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return service.activeNetworkInfo?.isConnected ?: false
}
