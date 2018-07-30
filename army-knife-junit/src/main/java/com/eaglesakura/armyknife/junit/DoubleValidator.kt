package com.eaglesakura.armyknife.junit

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue

/**
 * Validator for Double.
 */
class DoubleValidator(val actual: Double) {
    /**
     * the maximum delta.
     */
    var delta = 0.00001
        set(value) {
            if (value < 0) {
                throw IllegalArgumentException("Value is require to greater than 0.0.")
            }
        }

    fun eq(expected: Double): DoubleValidator {
        assertEquals(actual, expected, delta)
        return this
    }

    fun notEq(expected: Double): DoubleValidator {
        assertNotEquals(actual, expected, delta)
        return this
    }

    fun from(expected: Double): DoubleValidator {
        assertTrue("expected %f | actual %f".format(expected, actual), expected <= actual)
        return this
    }

    fun to(expected: Double): DoubleValidator {
        assertTrue("expected %f | actual %f".format(expected, actual), actual <= expected)
        return this
    }

    fun absTo(expected: Double): DoubleValidator {
        assertTrue("expected %f | actual %f".format(expected, Math.abs(actual)), Math.abs(actual) <= expected)
        return this
    }
}
