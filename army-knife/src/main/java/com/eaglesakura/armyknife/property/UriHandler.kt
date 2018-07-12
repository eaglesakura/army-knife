package com.eaglesakura.armyknife.property

import android.content.ContentValues
import android.net.Uri

/**
 * Providerの特定URIに反応するProvider
 */
interface UriHandler {
    /**
     * get操作を行う
     *
     * @param uri      対象URI
     * @param command  対象コマンド
     * @param arguments 引数リスト
     * @return Cursorとして返すデータ
     */
    fun query(uri: Uri, command: String, arguments: Array<String>): ByteArray?

    /**
     * 挿入操作を行う
     *
     * @param uri     対象URI
     * @param command 対象コマンド
     * @param values  送信されてきたデータ
     */
    fun insert(uri: Uri, command: String, values: ContentValues)
}
