package com.eaglesakura.ktx.oneshotlivedata

class DataState<T>(val raw: T) {

    /**
     * Get raw event with cast to T.
     */
    inline fun <reified T2 : T> get(): T2? {
        return if (done) {
            null
        } else {
            raw as? T2
        }
    }

    /**
     * Get raw
     */
    fun getAny(): Any? {
        return if (done) {
            null
        } else {
            raw
        }
    }

    internal var state = State.Pending

    val done: Boolean
        get() = state == State.Done

    internal enum class State {
        Pending,
        Running,
        Done,
    }

    companion object {
        val EMPTY_DATA = object {
            override fun equals(other: Any?): Boolean {
                return false
            }

            override fun hashCode(): Int {
                return -1
            }

            override fun toString(): String {
                return "EMPTY_DATA"
            }
        }
    }
}

typealias EventDataState = DataState<Event>