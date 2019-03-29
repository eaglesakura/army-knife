package com.eaglesakura.firearm.property

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri

/**
 * Delegate for content provider for ContentProvider properties..
 * Should call instance methods from ContentProvider's "query()" and "insert()" methods.
 *
 * Content Provider example.
 * <provider android:authorities="${applicationId}.internal.properties.provider"
 *         android:name="replace.your.content.Provider"
 *         android:exported="false"/>
 */
class PropertiesProviderDelegate(private val context: Context, private val handler: UriHandler) {

    @Suppress("unused", "UNUSED_PARAMETER")
    fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor {
        val buffer = handler.query(uri, selection!!, selectionArgs!!)
        return if (buffer != null) {
            ByteArrayCursor(buffer)
        } else {
            ByteArrayCursor(ByteArray(0))
        }
    }

    @Suppress("unused", "UNUSED_PARAMETER")
    fun insert(uri: Uri, contentValues: ContentValues?): Uri {
        handler.insert(uri, contentValues!!.getAsString(CONTENT_KEY_COMMAND), contentValues)
        return uri
    }

    companion object {
        private const val CONTENT_KEY_COMMAND = "com.eaglesakura.prop.COMMAND"

        /**
         * 対応したContentProviderと接続し、byte配列を得る
         *
         * @param context context
         * @param uri target URI
         * @param command 実行コマンド
         * @param argments 実行引数
         */
        @SuppressLint("NewApi")
        fun query(context: Context, uri: Uri, command: String, argments: Array<String>?): ByteArray? {
            context.contentResolver.query(uri, null, command, argments, null).use { cursor ->
                return if (cursor != null) {
                    ByteArrayCursor.toByteArray(cursor)
                } else {
                    null
                }
            }
        }

        /**
         * 対応したContentProviderに命令を送る
         *
         * @param uri target URI
         * @param command 実行コマンド
         * @param values 実行引数
         */
        fun insert(context: Context, uri: Uri, command: String, values: ContentValues?) {
            var putValues = values
            if (putValues == null) {
                putValues = ContentValues()
            }
            putValues.put(CONTENT_KEY_COMMAND, command)
            context.contentResolver.insert(uri, putValues)
        }
    }
}
