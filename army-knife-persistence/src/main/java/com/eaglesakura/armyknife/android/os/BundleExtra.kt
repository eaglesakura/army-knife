package com.eaglesakura.armyknife.android.os

import kotlin.reflect.KProperty

interface BundleExtra<T> {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T)
}