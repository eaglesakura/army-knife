package com.eaglesakura.firearm.di

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Object<ReturnType> provider for Dependency Injection.
 *
 * @see ProviderRegistry
 */
class Provider<ReturnType, ArgumentType>(
        private val provider: (ArgumentType.() -> ReturnType)
) {
    private val lock = ReentrantLock()

    private var overwriteProvider: (ArgumentType.() -> ReturnType)? = null

    /**
     * Clear overwrite provider.
     * This provider function will rollback.
     */
    fun reset() {
        lock.withLock {
            overwriteProvider = null
        }
    }

    /**
     * Overwrite provider function.
     * If you rollback(to default) provider, then call this.reset() function.
     */
    fun overwrite(newProvider: ProviderFunction<ReturnType, ArgumentType>) = overwrite(newProvider.toFunction())

    /**
     * Overwrite provider function.
     * If you rollback(to default) provider, then call this.reset() function.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun overwrite(newProvider: (ArgumentType.() -> ReturnType)) {
        lock.withLock {
            overwriteProvider = newProvider
        }
    }

    /**
     * Call provider function.
     */
    operator fun invoke(arg: ArgumentType): ReturnType {
        val target = lock.withLock {
            overwriteProvider ?: provider
        }
        return target.invoke(arg)
    }
}