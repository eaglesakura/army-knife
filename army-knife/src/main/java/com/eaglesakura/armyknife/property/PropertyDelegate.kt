package com.eaglesakura.armyknife.property

import android.content.Context
import kotlin.reflect.KProperty

/**
 * アプリ内で管理するプロパティ（設定値）の基底クラス.
 *
 * 継承したクラスは `by getRemoteProperty()` 等のDelegateを生成することで、DatabaseやContentProviderに保存可能なプロパティを生成できる.
 */
@Suppress("unused")
class PropertyDelegate(private val context: Context, store: PropertyStore, private val group: String) {

    private val properties: Properties = Properties(store)

    /**
     * Make key-value store's key.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    var getKey: (name: String) -> String

    init {
        this.getKey = fun(name: String): String {
            if (group.isEmpty()) {
                return name
            }
            return "$group.$name"
        }
    }

    fun commit() {
        properties.commit()
    }

    fun clear() {
        properties.clear()
    }

    fun load() {
        properties.load()
    }

    fun transaction(block: () -> Unit) {
        load()
        try {
            block()
            commit()
        } catch (err: Exception) {
            load()
            throw err
        }
    }

    /**
     * データ保存のためのKey-Value Storeに接続するDelegateを生成する.
     * 対応している型が自動的に設定される.
     * 型は自動的に判別されるが、可読性については考慮すること.
     *
     * example)
     * var userName:String by getProperty("user_name", "your name")
     * var userId by getProperty("user_id", "not-set")
     */
    fun <T> getProperty(name: String, defValue: T): IProperty<T> {
        val defValueObj: Any = defValue as Any

        val result: Any = when (defValue.javaClass.kotlin) {
            Int::class -> IntProperty(getKey(name), defValueObj as Int)
            Long::class -> LongProperty(getKey(name), defValueObj as Long)
            Float::class -> FloatProperty(getKey(name), defValueObj as Float)
            Double::class -> DoubleProperty(getKey(name), defValueObj as Double)
            String::class -> StringProperty(getKey(name), defValueObj as String)
            else -> throw IllegalArgumentException("Class<${defValue.javaClass}> not supported")
        }

        @Suppress("UNCHECKED_CAST")
        return result as IProperty<T>
    }

    private fun byteArrayProperty(name: String): IProperty<ByteArray> {
        return ByteArrayProperty(getKey(name))
    }

    interface IProperty<T> {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): T

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T)
    }

    private inner class IntProperty(private val key: String, defValue: Int) : IProperty<Int> {
        init {
            properties.setProperty(key, defValue)
        }

        override operator fun getValue(thisRef: Any?, property: KProperty<*>): Int {
            return properties.getIntProperty(key)
        }

        override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
            properties.setProperty(key, value)
        }
    }

    private inner class LongProperty(private val key: String, defValue: Long) : IProperty<Long> {
        init {
            properties.setProperty(key, defValue)
        }

        override operator fun getValue(thisRef: Any?, property: KProperty<*>): Long {
            return properties.getLongProperty(key)
        }

        override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Long) {
            properties.setProperty(key, value)
        }
    }

    private inner class FloatProperty(private val key: String, defValue: Float) : IProperty<Float> {
        init {
            properties.setProperty(key, defValue)
        }

        override operator fun getValue(thisRef: Any?, property: KProperty<*>): Float {
            return properties.getFloatProperty(key)
        }

        override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Float) {
            properties.setProperty(key, value)
        }
    }

    private inner class DoubleProperty(private val key: String, defValue: Double) : IProperty<Double> {
        init {
            properties.setProperty(key, defValue)
        }

        override operator fun getValue(thisRef: Any?, property: KProperty<*>): Double {
            return properties.getDoubleProperty(key)
        }

        override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Double) {
            properties.setProperty(key, value)
        }
    }

    private inner class StringProperty(private val key: String, defValue: String) : IProperty<String> {
        init {
            properties.setProperty(key, defValue)
        }

        override operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
            return properties.getStringProperty(key)
        }

        override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
            properties.setProperty(key, value)
        }
    }

    private inner class ByteArrayProperty(private val key: String) : IProperty<ByteArray> {
        override operator fun getValue(thisRef: Any?, property: KProperty<*>): ByteArray {
            return properties.getByteArrayProperty(key) ?: ByteArray(0)
        }

        override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: ByteArray) {
            properties.setProperty(key, value)
        }
    }
}