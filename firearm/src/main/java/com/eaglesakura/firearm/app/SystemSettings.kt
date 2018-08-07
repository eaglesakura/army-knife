package com.eaglesakura.firearm.app

import android.content.Context
import android.os.Build
import com.eaglesakura.armyknife.property.PropertyDelegate
import com.eaglesakura.armyknife.property.TextDatabasePropertyStore

/**
 * Slothシステム設定保持クラス.
 *
 * アップデート検出, インストール時に位置に確定するUID設定などを行う.
 */
internal class SystemSettings(context: Context) {

    private val properties = PropertyDelegate(context, TextDatabasePropertyStore(context, "sloth-system-v5.db"), "")

    /**
     * 前回起動時のVersion Code
     */
    var lastBootedAppVersionCode: Long by properties.getProperty("lastBootedAppVersionCode", -1L)

    /**
     * 前回起動時のVersion Name
     */
    var lastBootedAppVersionName: String by properties.getProperty("lastBootedAppVersionName", "")

    /**
     * インストール時に一意に確定する文字列.
     *
     * アプリが再インストールされた場合はリセットされる.
     */
    var installUniqueId: String by properties.getProperty("installUniqueId", "")

    /**
     * 最後に起動されたときのAPIレベル
     *
     * Android Oreoであれば27となる.
     * @see Build.VERSION.SDK_INT
     */
    var lastBootedApiLevel: Int by properties.getProperty("lastBootedOsVersion", Build.VERSION.SDK_INT)

    init {
        properties.load()
    }

    suspend fun transaction(block: suspend () -> Unit) = properties.transaction(block)
}
