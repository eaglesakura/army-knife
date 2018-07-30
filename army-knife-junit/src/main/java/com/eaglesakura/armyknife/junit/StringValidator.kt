package com.eaglesakura.armyknife.junit

import org.junit.Assert.assertTrue

/**
 * Validator for String.
 */
class StringValidator(var actual: String) {
    fun contains(expected: CharSequence) {
        assertTrue("contains | expected \"%s\" | actual \"%s\"".format(expected, actual), actual.contains(expected))
    }

    fun notContains(expected: CharSequence) {
        assertTrue("not contains | expected \"%s\" | actual \"%s\"".format(expected, actual), !actual.contains(expected))
    }
}
