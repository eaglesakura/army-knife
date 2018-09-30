package com.eaglesakura.firearm.di

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class Provider<ReturnType, ArgumentType>(
        val provider: (ArgumentType.() -> ReturnType)
) {
    private val lock = ReentrantLock()

    private var overwriteProvider: (ArgumentType.() -> ReturnType)? = null

    internal fun reset() {
        lock.withLock {
            overwriteProvider = null
        }
    }

    /**
     * Run provider.
     */
    operator fun invoke(arg: ArgumentType): ReturnType {
        val target = lock.withLock {
            overwriteProvider ?: provider
        }
        return target.invoke(arg)
    }
}