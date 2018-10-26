package com.eaglesakura.armyknife.runtime.extensions

import kotlin.reflect.KClass


/**
 * check any instance implemented `Class<>`.
 */
fun Any.instanceOf(clazz: Class<*>): Boolean {
    return try {
        javaClass.asSubclass(clazz) != null
    } catch (e: Exception) {
        false
    }
}

/**
 * check any instance implemented `KClass<>`.
 */
fun Any.instanceOf(clazz: KClass<*>): Boolean = instanceOf(clazz.java)