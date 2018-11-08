@file:Suppress("unused")

package com.eaglesakura.armyknife.android.extensions

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer


/**
 * Observe data when Lifecycle alive.
 * This method call observe always(Example, Activity/Fragment paused and more).
 * If observer should handle data every time and always, May use this method.
 *
 * e.g.)
 * fun onCreate() {
 *      exampleValue.observeAlive(this) { value ->
 *          // do something,
 *          // this message receive on pausing.
 *      }
 *      exampleValue.observe(this) { value ->
 *          // do something,
 *          // this message "NOT" receive on pausing.
 *      }
 * }
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
fun <T> LiveData<T>.observeAlive(owner: LifecycleOwner, observer: Observer<T>) {
    observeForever(observer)
    owner.lifecycle.subscribe {
        if (it == Lifecycle.Event.ON_DESTROY) {
            removeObserver(observer)
        }
    }
}
