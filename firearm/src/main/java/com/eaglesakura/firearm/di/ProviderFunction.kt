package com.eaglesakura.firearm.di

interface ProviderFunction<ReturnType, ArgumentType> {

    /**
     * Interface to Function.
     */
    fun asFunction(): (ArgumentType.() -> ReturnType) {
        val function = this
        return {
            function(this)
        }
    }

    operator fun invoke(argument: ArgumentType): ReturnType
}