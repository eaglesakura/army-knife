package com.eaglesakura.armyknife.property

import java.util.*

/**
 * Text Key-Valueのプロパティを保持する
 */
open class TextPropertyStore : PropertyStore {

    /**
     * ロード済みのプロパティ
     */
    protected val propMap: MutableMap<String, Property> = HashMap()

    override fun getStringProperty(key: String): String {
        return propMap[key]?.value ?: throw IllegalStateException("Key[$key] not found.")
    }

    override fun setProperty(key: String, value: String) {
        var prop: Property? = propMap[key]
        if (prop == null) {
            prop = Property(key, value)
            propMap[key] = prop
        } else {
            prop.value = value
            prop.modified = true
        }
    }

    override fun clear() {
        for ((_, prop) in propMap) {
            if (prop.value != prop.defaultValue) {
                prop.modified = true
            }
            prop.value = prop.defaultValue
        }
    }

    /**
     * Key-ValueのMAPに変換する
     */
    @Suppress("unused")
    fun asMap(): Map<String, String> {
        val datas = HashMap<String, String>()
        for ((_, prop) in propMap) {
            if (!prop.value.isEmpty()) {
                datas[prop.key] = prop.value
            } else {
                datas[prop.key] = prop.defaultValue
            }
        }
        return datas
    }

    /**
     * Mapから復元する
     */
    @Suppress("unused")
    fun loadProperties(map: Map<String, String>) {
        for ((key, value) in map) {
            val property = propMap[key]
            if (property != null) {
                property.value = value
                property.modified = true
            }
        }
    }

    override fun commit() {}

    override fun load() {}


    /**
     * テキストで保持されたプロパティ
     */
    protected class Property internal constructor(
        /**
         * データベース用のkey
         */
        internal val key: String,
        /**
         * 現在の値
         */
        internal var value: String
    ) {

        /**
         * デフォルト値
         */
        internal val defaultValue: String = value

        /**
         * 読み込み後、値を更新していたらtrue
         */
        internal var modified = false

    }
}
