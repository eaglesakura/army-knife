package com.eaglesakura.ktx.property

/**
 * Textで管理されたプロパティを扱う
 */
interface PropertyStore {
    /**
     * Get property from in-memory.
     */
    fun getStringProperty(key: String): String

    /**
     * Save property to in-memory.
     */
    fun setProperty(key: String, value: String)

    /**
     * Reset all properties to default.
     */
    fun clear()

    /**
     * All properties save to database.
     */
    fun commit()

    /**
     * Load properties from database.
     * If in-memory-value is newer than database-value, Preferentially use to database-value.
     */
    fun load()
}
