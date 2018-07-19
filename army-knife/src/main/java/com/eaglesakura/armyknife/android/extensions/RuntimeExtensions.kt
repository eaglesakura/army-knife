package com.eaglesakura.armyknife.android.extensions


fun <K, V> Map<K, V>.findKey(selector: (value: V) -> Boolean): K? {
    this.entries.forEach {
        if (selector(it.value)) {
            return it.key
        }
    }
    return null
}