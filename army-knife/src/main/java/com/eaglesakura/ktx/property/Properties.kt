@file:Suppress("unused")

package com.eaglesakura.ktx.property

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.eaglesakura.ktx.runtime.extensions.decodeBase64
import com.eaglesakura.ktx.runtime.extensions.encodeBase64
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*

/**
 * 簡易設定用のプロパティを保持するためのクラス
 */
class Properties(private val propertyStore: PropertyStore) {
    fun getStringProperty(key: String): String {
        return propertyStore.getStringProperty(key)
    }

    fun setProperty(key: String, value: Any) {
        var commitValue: Any? = value
        when (commitValue) {
            is Enum<*> -> commitValue = commitValue.name
            is Bitmap -> commitValue =
                    try {
                        val os = ByteArrayOutputStream()
                        commitValue.compress(Bitmap.CompressFormat.PNG, 100, os)
                        os.toByteArray()
                    } catch (e: Exception) {
                        null
                    }
            is Boolean -> // trueならば"1"、falseならば"0"としてしまう
                commitValue = if (java.lang.Boolean.TRUE == commitValue) "1" else "0"
        }

        if (commitValue is ByteArray) {
            propertyStore.setProperty(key, commitValue.encodeBase64())
        } else {
            assert(commitValue != null)
            propertyStore.setProperty(key, commitValue.toString())
        }
    }

    fun clear() {
        propertyStore.clear()
    }

    fun commit() {
        propertyStore.commit()
    }

    fun load() {
        propertyStore.load()
    }

    fun getIntProperty(key: String): Int {
        return Integer.parseInt(getStringProperty(key))
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun getLongProperty(key: String): Long {
        return java.lang.Long.parseLong(getStringProperty(key))
    }

    fun getDateProperty(key: String): Date {
        return Date(getLongProperty(key))
    }

    fun getFloatProperty(key: String): Float {
        return java.lang.Float.parseFloat(getStringProperty(key))
    }

    fun getBooleanProperty(key: String): Boolean {
        val value = getStringProperty(key)

        // 保存速度を向上するため、0|1判定にも対応する
        if ("0" == value) {
            return false
        } else if ("1" == value) {
            return true
        }
        return java.lang.Boolean.parseBoolean(getStringProperty(key))
    }

    fun getDoubleProperty(key: String): Double {
        return java.lang.Double.parseDouble(getStringProperty(key))
    }

    /**
     * 画像ファイル形式で保存してあるBitmapを取得する
     */
    fun getImageProperty(key: String): Bitmap? {
        val imageFile = getByteArrayProperty(key) ?: return null

        try {
            return BitmapFactory.decodeStream(ByteArrayInputStream(imageFile))
        } catch (ignored: Exception) {
        }

        return null
    }

    /**
     * base64エンコードオブジェクトを取得する
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun getByteArrayProperty(key: String): ByteArray? {
        return try {
            getStringProperty(key).decodeBase64()
        } catch (e: Exception) {
            null
        }

    }
}
