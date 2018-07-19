package com.eaglesakura.armyknife.junit

import com.eaglesakura.junit.DoubleValidator
import com.eaglesakura.junit.LongValidator

fun Float.validate(action: DoubleValidator.() -> Unit) = DoubleValidator(toDouble()).let {
    action(it)
    it.get()!!
}

fun Int.validate(action: LongValidator.() -> Unit) = LongValidator(toLong()).let {
    action(it)
    it.get()!!.toInt()
}

fun Long.validate(action: LongValidator.() -> Unit) = LongValidator(toLong()).let {
    action(it)
    it.get()!!
}