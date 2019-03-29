package com.eaglesakura.armyknife.runtime

/**
 * Returns random value.
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
object Random {

    @JvmStatic
    val rand = java.util.Random()

    @JvmStatic
    fun boolean(): Boolean {
        return rand.nextBoolean()
    }

    @JvmStatic
    fun int8(): Byte {
        return rand.nextInt().toByte()
    }

    @JvmStatic
    fun int16(): Short {
        return rand.nextInt().toShort()
    }

    @JvmStatic
    fun int32(): Int {
        return rand.nextInt()
    }

    @JvmStatic
    fun int64(): Long {
        return rand.nextLong()
    }

    /**
     * Generate unsigned.
     */
    @JvmStatic
    fun uint8(): Byte {
        return (rand.nextInt() and 0x7F).toByte()
    }

    /**
     * Generate unsigned.
     */
    @JvmStatic
    fun uint16(): Short {
        return (rand.nextInt() and 0x00007FFF).toShort()
    }

    /**
     * Generate unsigned.
     */
    @JvmStatic
    fun uint32(): Int {
        return rand.nextInt() and 0x7FFFFFFF
    }

    /**
     * Generate unsigned.
     */
    @JvmStatic
    fun uint64(): Long {
        return rand.nextLong() and 0x7FFFFFFFFFFFFFFFL
    }

    /**
     * 0.0 to 1.0
     */
    @JvmStatic
    fun float32(): Float {
        return rand.nextFloat()
    }

    /**
     * 0.0 to 1.0
     */
    @JvmStatic
    fun float64(): Double = rand.nextDouble()

    /**
     * Random[a-z, 0-9, A-Z]
     */
    @JvmStatic
    fun ascii(): Byte {
        return when (uint8() % 5) {
            0, 1 -> ('a'.toInt() + uint8() % 26).toByte()
            2, 3 -> ('A'.toInt() + uint8() % 26).toByte()
            else -> ('0'.toInt() + uint8() % 10).toByte()
        }
    }

    /**
     * Returns random strings.
     *
     * @author @eaglesakura
     * @link https://github.com/eaglesakura/army-knife
     */
    @JvmStatic
    fun string(length: Int = 32): String {
        val buffer = ByteArray(length)
        for (i in 0 until length) {
            buffer[i] = ascii()
        }
        return String(buffer)
    }

    /**
     * Returns random short strings(less than 10chars).
     *
     * @author @eaglesakura
     * @link https://github.com/eaglesakura/army-knife
     */
    @JvmStatic
    fun smallString(): String {
        return string(6) + (Random.uint16().toInt() and 0xFF)
    }

    /**
     * Returns random large strings(1024 chars).
     *
     * @author @eaglesakura
     * @link https://github.com/eaglesakura/army-knife
     */
    @JvmStatic
    fun largeString(): String {
        return string(4 * 256)
    }

    @JvmStatic
    fun byteArray(length: Int = 32 + uint8()): ByteArray {
        val buffer = ByteArray(length)
        for (i in buffer.indices) {
            buffer[i] = int8()
        }
        return buffer
    }

    @JvmStatic
    fun <T : Enum<*>> enumerate(clazz: Class<T>): T {
        val valuesMethod = clazz.getMethod("values")
        val values = valuesMethod.invoke(clazz) as Array<*>
        @Suppress("UNCHECKED_CAST")
        return values[uint8() % values.size] as T
    }

    @JvmStatic
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