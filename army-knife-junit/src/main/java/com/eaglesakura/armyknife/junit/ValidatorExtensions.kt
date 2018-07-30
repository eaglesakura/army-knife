package com.eaglesakura.armyknife.junit

inline fun Float.validate(action: DoubleValidator.() -> Unit) = DoubleValidator(toDouble()).let {
    action(it)
    it.actual.toFloat()
}

inline fun Double.validate(action: DoubleValidator.() -> Unit) = DoubleValidator(this).let {
    action(it)
    it.actual
}

inline fun Byte.validate(action: LongValidator.() -> Unit) = LongValidator(toLong()).let {
    action(it)
    it.actual.toByte()
}

inline fun Short.validate(action: LongValidator.() -> Unit) = LongValidator(toLong()).let {
    action(it)
    it.actual.toShort()
}

inline fun Int.validate(action: LongValidator.() -> Unit) = LongValidator(toLong()).let {
    action(it)
    it.actual.toInt()
}

inline fun Long.validate(action: LongValidator.() -> Unit) = LongValidator(toLong()).let {
    action(it)
    it.actual
}

fun String.validate(action: StringValidator.() -> Unit) = StringValidator(this).let {
    action(it)
    it.actual
}
