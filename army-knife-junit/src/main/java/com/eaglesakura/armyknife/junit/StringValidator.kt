package com.eaglesakura.armyknife.junit

import org.junit.Assert.assertTrue

/**
 * Validator for String.
 */
@Deprecated("Use assertJ library, delete soon.")
class StringValidator(var actual: String) {
    fun contains(expected: CharSequence) {
        assertTrue("contains | expected \"%s\" | actual \"%s\"".format(expected, actual), actual.contains(expected))
    }

    fun notContains(expected: CharSequence) {
        assertTrue("not contains | expected \"%s\" | actual \"%s\"".format(expected, actual), !actual.contains(expected))
    }

    fun startsWith(expected: CharSequence) {
        assertTrue("not startsWith | expected \"%s\" | actual \"%s\"".format(expected, actual), actual.startsWith(expected))
    }

    fun endsWith(expected: CharSequence) {
        assertTrue("not endsWith | expected \"%s\" | actual \"%s\"".format(expected, actual), actual.endsWith(expected))
    }
}
