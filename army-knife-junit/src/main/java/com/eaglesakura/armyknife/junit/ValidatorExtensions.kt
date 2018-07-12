package com.eaglesakura.armyknife.junit

import com.eaglesakura.junit.DoubleValidator

fun Float.validate(action: DoubleValidator.() -> Unit) = DoubleValidator(toDouble()).let {
    action(it)
    it.get()!!
}
