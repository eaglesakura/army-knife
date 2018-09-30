package com.eaglesakura.firearm.di

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Dependency Injection Provider registry.
 *
 * This class use to Factory/Builder pattern provider.
 * You will make a Factory class, and Builder class.
 * In unit test, Provider will changed to Mock or such provider.
 *
 * object FooClassFactory {
 *      val provider = ProviderRegistry.newProvider<FooClass, Builder> { arg ->
 *          // return it
 *      }
 *
 *      class Builder {
 *
 *          fun build() : Foo = provider(this)
 *      }
 * }
 */
class ProviderRegistry {

    private val lock = ReentrantLock()

    private val registries = ArrayList<Provider<*, *>>()

    /**
     * Make provider from Interface.
     */
    fun <ReturnType, ArgumentType> newProvider(defaultProvider: ProviderFunction<ReturnType, ArgumentType>): Provider<ReturnType, ArgumentType> = newProvider(defaultProvider.asFunction())

    /**
     * Make provider.
     */
    fun <ReturnType, ArgumentType> newProvider(defaultProvider: (ArgumentType.() -> ReturnType)): Provider<ReturnType, ArgumentType> {
        return lock.withLock {
            val result = Provider(defaultProvider)
            registries.add(result)
            return@withLock result
        }
    }

    /**
     * Reset overwrite providers.
     */
    fun reset() {
        lock.withLock {
            for (provider in registries) {
                provider.reset()
            }
        }
    }
}