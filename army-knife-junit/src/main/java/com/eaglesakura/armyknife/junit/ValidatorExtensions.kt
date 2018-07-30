package com.eaglesakura.armyknife.junit

import com.eaglesakura.junit.*

inline fun Float.validate(action: DoubleValidator.() -> Unit) = DoubleValidator(toDouble()).let {
    action(it)
    it.get()!!.toFloat()
}

inline fun Double.validate(action: DoubleValidator.() -> Unit) = DoubleValidator(this).let {
    action(it)
    it.get()!!
}

inline fun Byte.validate(action: LongValidator.() -> Unit) = LongValidator(toLong()).let {
    action(it)
    it.get()!!.toByte()
}

inline fun Short.validate(action: LongValidator.() -> Unit) = LongValidator(toLong()).let {
    action(it)
    it.get()!!.toShort()
}

inline fun Int.validate(action: LongValidator.() -> Unit) = LongValidator(toLong()).let {
    action(it)
    it.get()!!.toInt()
}

inline fun Long.validate(action: LongValidator.() -> Unit) = LongValidator(toLong()).let {
    action(it)
    it.get()!!
}

fun String.validate(action: StringValidator.() -> Unit) = StringValidator(this).let {
    action(it)
    it.get()!!
}

fun <E, T : Collection<E>> T.validate(action: CollectionValidator<E>.() -> Unit) = CollectionValidator(this).let {
    action(it)
    it.get()!!
}
