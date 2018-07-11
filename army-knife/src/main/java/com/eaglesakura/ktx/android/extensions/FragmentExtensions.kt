package com.eaglesakura.ktx.android.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * このメソッドはFragmentManagerから条件にマッチするFragmentを探索して返却する.
 * finder()は渡されるFragmentをチェックし、対象であればtrueを返却する.
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
 */
inline fun <reified T> Fragment.findInterface(): T? {
    if (activity is T) {
        return activity as T
    }

    // 親を探索
    var target = parentFragment
    while (target != null) {
        if (target is T) {
            return target
        }
        target = target.parentFragment
    }

    return null
}