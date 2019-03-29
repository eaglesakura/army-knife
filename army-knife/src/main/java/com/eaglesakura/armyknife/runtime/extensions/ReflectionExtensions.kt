package com.eaglesakura.armyknife.runtime.extensions

import kotlin.reflect.KClass

/**
 * check any instance implemented `Class<>`.
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
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
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
fun Any.instanceOf(clazz: KClass<*>): Boolean = instanceOf(clazz.java)