package com.eaglesakura.firearm.di

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class Provider<ReturnType, ArgumentType>(
        val provider: (ArgumentType.() -> ReturnType)
) {
    private val lock = ReentrantLock()

    private var overrideProvider: (ArgumentType.() -> ReturnType)? = null

    internal fun reset() {
        lock.withLock {
            overrideProvider = null
        }
    }

    /**
     * Run provider.
     */
    operator fun invoke(arg: ArgumentType): ReturnType {
        val target = lock.withLock {
            overrideProvider ?: provider
        }
        return target.invoke(arg)
    }
}