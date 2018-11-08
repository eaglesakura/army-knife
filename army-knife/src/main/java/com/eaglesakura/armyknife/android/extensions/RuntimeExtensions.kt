package com.eaglesakura.armyknife.android.extensions

/**
 * find "K" from map.
 *
 * val map = mapOf(
 *      Pair("Key", "Value")
 * )
 * val key = map.findKey { value ->
 *      value == "Value"
 * }
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
@Deprecated(
    "replace to package com.eaglesakura.armyknife.runtime.extensions",
    ReplaceWith("com.eaglesakura.armyknife.runtime.extensions.findKey")
)
fun <K, V> Map<K, V>.findKey(selector: (value: V) -> Boolean): K? {
    this.entries.forEach {
        if (selector(it.value)) {
            return it.key
        }
    }
    return null
}