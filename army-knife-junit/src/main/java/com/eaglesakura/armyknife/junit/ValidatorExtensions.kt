package com.eaglesakura.armyknife.junit

@Deprecated("Use assertJ library, delete soon.")
inline fun Float.validate(action: DoubleValidator.() -> Unit) = DoubleValidator(toDouble()).let {
    action(it)
    it.actual.toFloat()
}

@Deprecated("Use assertJ library, delete soon.")
inline fun Double.validate(action: DoubleValidator.() -> Unit) = DoubleValidator(this).let {
    action(it)
    it.actual
}

@Deprecated("Use assertJ library, delete soon.")
inline fun Byte.validate(action: LongValidator.() -> Unit) = LongValidator(toLong()).let {
    action(it)
    it.actual.toByte()
}

@Deprecated("Use assertJ library, delete soon.")
inline fun Short.validate(action: LongValidator.() -> Unit) = LongValidator(toLong()).let {
    action(it)
    it.actual.toShort()
}

@Deprecated("Use assertJ library, delete soon.")
inline fun Int.validate(action: LongValidator.() -> Unit) = LongValidator(toLong()).let {
    action(it)
    it.actual.toInt()
}

@Deprecated("Use assertJ library.")
inline fun Long.validate(action: LongValidator.() -> Unit) = LongValidator(toLong()).let {
    action(it)
    it.actual
}

@Deprecated("Use assertJ library.")
fun String.validate(action: StringValidator.() -> Unit) = StringValidator(this).let {
    action(it)
    it.actual
}
