package com.eaglesakura.armyknife.runtime


/**
 * Returns random value.
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
object Random {

    val rand = java.util.Random()

    fun boolean(): Boolean {
        return rand.nextBoolean()
    }

    fun int8(): Byte {
        return rand.nextInt().toByte()
    }

    fun int16(): Short {
        return rand.nextInt().toShort()
    }

    fun int32(): Int {
        return rand.nextInt()
    }

    fun int64(): Long {
        return rand.nextLong()
    }

    /**
     * Generate unsigned.
     */
    fun uint8(): Byte {
        return (rand.nextInt() and 0x7F).toByte()
    }

    /**
     * Generate unsigned.
     */
    fun uint16(): Short {
        return (rand.nextInt() and 0x00007FFF).toShort()
    }

    /**
     * Generate unsigned.
     */
    fun uint32(): Int {
        return rand.nextInt() and 0x7FFFFFFF
    }

    /**
     * Generate unsigned.
     */
    fun uint64(): Long {
        return rand.nextLong() and 0x7FFFFFFFFFFFFFFFL
    }

    /**
     * 0.0 to 1.0
     */
    fun float32(): Float {
        return rand.nextFloat()
    }

    /**
     * 0.0 to 1.0
     */
    fun float64(): Double = rand.nextDouble()

    /**
     * Random[a-z, 0-9, A-Z]
     */
    fun ascii(): Byte {
        return when (uint8() % 5) {
            0, 1 -> ('a'.toInt() + uint8() % 26).toByte()
            2, 3 -> ('A'.toInt() + uint8() % 26).toByte()
            else -> ('0'.toInt() + uint8() % 10).toByte()
        }
    }

    fun string(length: Int = 32): String {
        val buffer = ByteArray(length)
        for (i in 0 until length) {
            buffer[i] = ascii()
        }
        return String(buffer)
    }

    fun smallString(): String {
        return string(6) + (Random.uint16().toInt() and 0xFF)
    }

    fun largeString(): String {
        return string(4 * 256)
    }

    fun byteArray(length: Int = 32 + uint8()): ByteArray {
        val buffer = ByteArray(length)
        for (i in buffer.indices) {
            buffer[i] = int8()
        }
        return buffer
    }

    fun <T : Enum<*>> enumerate(clazz: Class<T>): T {
        val valuesMethod = clazz.getMethod("values")
        val values = valuesMethod.invoke(clazz) as Array<*>
        @Suppress("UNCHECKED_CAST")
        return values[uint8() % values.size] as T
    }

    fun <T : Enum<*>> enumerateOrNull(clazz: Class<T>): T? {
        val valuesMethod = clazz.getMethod("values")
        val values = valuesMethod.invoke(clazz) as Array<*>
        return if (uint8() % (values.size + 1) == 0) {
            null
        } else {
            @Suppress("UNCHECKED_CAST")
            values[uint8() % values.size] as T
        }
    }
}