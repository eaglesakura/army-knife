package com.eaglesakura.ktx.oneshotlivedata

class EventId(private val name: String) : Event {
    override val id: Any
        get() = name

    override fun toString(): String {
        return name
    }

    /**
     * This instance is only-one.
     */
    final override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    /**
     * This instance is only-one.
     */
    final override fun hashCode(): Int {
        return super.hashCode()
    }
}
