package com.eaglesakura.firearm.di

/**
 * Functional interface for DI provider.
 */
interface ProviderFunction<ReturnType, ArgumentType> {

    /**
     * Interface to Function.
     */
    fun toFunction(): (ArgumentType.() -> ReturnType) {
        val function = this
        return {
            function(this)
        }
    }

    operator fun invoke(argument: ArgumentType): ReturnType
}