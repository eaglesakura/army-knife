package com.eaglesakura.ktx.sloth.arch

import androidx.annotation.UiThread
import androidx.lifecycle.Lifecycle
import com.eaglesakura.ktx.android.extensions.subscribe
import kotlin.reflect.KClass

/**
 * The DataStore is chain with lifecycle.
 * When lifecycle destroyed, Stored data too all destroy.
 *
 * If stored data implements the LifecycleData interface,
 * Call "onDestroy()" method on destroy time.
 */
object LifecycleDataStore {

    private val containers: MutableMap<Lifecycle, LifecycleValue> = mutableMapOf()

    @UiThread
    fun put(owner: Lifecycle, value: Any) {
        var lifecycleValue = containers[owner]
        if (lifecycleValue == null) {
            lifecycleValue = LifecycleValue()
            containers[owner] = lifecycleValue

            // destroy data.
            owner.subscribe {
                if (it != Lifecycle.Event.ON_DESTROY) {
                    return@subscribe
                }

                // message destroy.
                containers[owner]?.values?.values?.forEach { v ->
                    (v as? LifecycleData)?.onDestroy(owner)
                }
                containers.remove(owner)
            }
        }

        lifecycleValue.put(value)
    }

    @UiThread
    fun <T> get(owner: Lifecycle, clazz: KClass<*>): T? {
        return containers[owner]?.get(clazz)
    }

    internal class LifecycleValue {
        val values: MutableMap<String, Any> = mutableMapOf()

        fun put(obj: Any, name: String = "") {
            if (name.isEmpty()) {
                values[obj.javaClass.name] = obj
            } else {
                values[name] = obj
            }
        }

        fun <T> get(clazz: KClass<*>, name: String = ""): T? {
            return if (name.isEmpty()) {
                @Suppress("UNCHECKED_CAST")
                values[clazz.java.name] as? T
            } else {
                @Suppress("UNCHECKED_CAST")
                values[name] as? T
            }
        }
    }
}