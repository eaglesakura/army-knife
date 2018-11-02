@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.eaglesakura.firearm.app

import android.os.Build

/**
 * バージョンアップ情報
 */
data class VersionContext(
    /**
     * 前回起動時のバージョン名を取得する
     */
    val oldVersionName: String,
    /**
     * 前回起動時のバージョンコードを取得する
     */
    val oldVersionCode: Long,
    /**
     * 現在のバージョン名を取得する
     */
    val versionName: String,
    /**
     * 現在のバージョンコードを取得する
     */
    val versionCode: Long,

    /**
     * 前回起動時のAPIレベル
     */
    val oldApiLevel: Int
) {

    /**
     * 前回起動からバージョンアップされている場合true
     */
    val versionUpdated: Boolean
        get() = oldVersionCode != versionCode

    /**
     * 前回起動からOSバージョンアップされている場合true
     */
    val osVersionUpdated: Boolean
        get() = Build.VERSION.SDK_INT > oldApiLevel
}
