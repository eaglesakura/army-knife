package com.eaglesakura.armyknife.android.extensions

import androidx.fragment.app.FragmentManager
import com.eaglesakura.armyknife.runtime.extensions.instanceOf

/**
 * Find "T" instance from All FragmentManager tree.
 * Not found then returns null value.
 */
inline fun <reified T> FragmentManager.deepFindInterface(): T? = deepFindInterface(T::class.java)

fun <T> FragmentManager.deepFindInterface(clazz: Class<*>): T? {
    fragments.forEach { fragment ->
        if (fragment.instanceOf(clazz)) {
            @Suppress("UNCHECKED_CAST")
            return fragment as T
        }
        fragment.childFragmentManager.deepFindInterface<T>(clazz)?.also {
            return it
        }
    }
    return null
}