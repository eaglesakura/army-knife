package com.eaglesakura.armyknife.junit

import org.junit.Assert.*

/**
 * Validator for Long.
 */
@Deprecated("Use assertJ library, delete soon.")
class LongValidator(val actual: Long) {
    fun from(expected: Long) {
        assertTrue("expected %d | actual %d".format(expected, actual), expected <= actual)
    }

    fun to(expected: Long) {
        assertTrue("expected %d | actual %d".format(expected, actual), actual <= expected)
    }

    /**
     * an actual should be less than an expected.
     */
    fun less(expected: Long) {
        assertTrue("expected %d | actual %d".format(expected, actual), actual < expected)
    }
}
