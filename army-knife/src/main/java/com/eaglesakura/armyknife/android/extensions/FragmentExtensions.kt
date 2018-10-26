package com.eaglesakura.armyknife.android.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.eaglesakura.armyknife.runtime.extensions.instanceOf
import kotlin.reflect.KClass

/**
 * このメソッドはFragmentManagerから条件にマッチするFragmentを探索して返却する.
 * finder()は渡されるFragmentをチェックし、対象であればtrueを返却する.
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
@Suppress("UNCHECKED_CAST")
fun <T : Fragment> FragmentManager.find(finder: (frag: Fragment) -> Boolean): T? {
    fragments.forEach { fragment ->
        if (finder(fragment)) {
            return fragment as T
        }
    }
    return null
}

/**
 * Finding the "T" Interface from Activity or parent fragments.
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
@Suppress("UNCHECKED_CAST")
fun <T : Any> Fragment.findInterface(clazz: KClass<T>): T? {
    // find from parent
    var target: Fragment? = this
    while (target != null) {
        if (target.instanceOf(clazz)) {
            return target as T
        }
        target = target.parentFragment
    }

    if (activity?.instanceOf(clazz) == true) {
        return activity as T
    }

    return null
}

/**
 * Finding the "T" Interface from Activity or parent fragments.
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
inline fun <reified T> Fragment.findInterface(): T? {
    // find from parent
    var target: Fragment? = this
    while (target != null) {
        if (target is T) {
            return target
        }
        target = target.parentFragment
    }

    if (activity is T) {
        return activity as T
    }

    return null
}