package com.eaglesakura.armyknife.android

import android.content.ContentResolver
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.net.toUri

object ApplicationResources {

    /**
     * Convert resource-id-int to Uri.
     * Use to ImageLibrary, NetworkLibrary, or else.
     *
     * @author @eaglesakura
     * @link https://github.com/eaglesakura/army-knife
     */
    @JvmStatic
    fun getDrawableUri(context: Context, @DrawableRes id: Int): Uri {
        return buildString {
            append(ContentResolver.SCHEME_ANDROID_RESOURCE)
            append("://")
            append(context.resources.getResourcePackageName(id))
            append("/")
            append(context.resources.getResourceTypeName(id))
            append("/")
            append(context.resources.getResourceEntryName(id))
        }.toUri()
    }

    /**
     * Convert resource-id-int to Uri.
     * Use to ImageLibrary, NetworkLibrary, or else.
     */
    @JvmStatic
    fun getDrawableUri(context: Context, resName: String): Uri {
        @DrawableRes val id = context.resources.getIdentifier(
                resName,
                "string",
                context.packageName
        )
        return getDrawableUri(context, id)
    }


    /**
     * Load drawable resource with set tint color to it.
     */
    @JvmStatic
    fun getDrawable(context: Context, @DrawableRes resId: Int, @ColorInt tint: Int = 0): Drawable {
        val result = ResourcesCompat.getDrawable(context.resources, resId, context.theme)!!
        if (tint != 0) {
            DrawableCompat.setTint(result, tint)
        }
        return result
    }

    /**
     * string xmlリソース名から文字列リソースを取得する
     */
    @JvmStatic
    fun getStringFromIdName(context: Context, resName: String): String? {
        return try {
            val id = context.resources.getIdentifier(
                    resName,
                    "string",
                    context.packageName
            )
            context.resources.getString(id)
        } catch (e: Exception) {
            null
        }

    }

    /**
     * string xmlリソース名から文字列リソースを取得する
     */
    @JvmStatic
    fun getStringFromIdName(context: Context, resName: String, vararg arg: Any): String? {
        return try {
            val id = context.resources.getIdentifier(
                    resName,
                    "string",
                    context.packageName
            )

            context.resources.getString(id, *arg)
        } catch (e: Exception) {
            null
        }

    }

    /**
     * string xmlリソース名から文字列リソースを取得する
     */
    @JvmStatic
    fun getIntegerFromIdName(context: Context, resName: String): Int? {
        return try {
            val id = context.resources.getIdentifier(
                    resName,
                    "integer",
                    context.packageName
            )
            context.resources.getInteger(id)
        } catch (e: Exception) {
            null
        }

    }

}