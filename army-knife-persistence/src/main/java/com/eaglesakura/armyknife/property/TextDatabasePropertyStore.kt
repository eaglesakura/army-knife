package com.eaglesakura.armyknife.property

import android.content.Context
import com.eaglesakura.armyknife.android.db.TextKeyValueStore
import java.io.File
import java.util.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Key-Value Store by SQLite.
 */
class TextDatabasePropertyStore(private val context: Context, private val databasePath: File) : TextPropertyStore() {

    private val lock = ReentrantLock()

    constructor(context: Context, dbName: String) : this(context, context.getDatabasePath(dbName))

    override fun commit() {
        lock.withLock {
            val commitValues = HashMap<String, String>().also { map ->
                for ((_, property) in propMap) {
                    if (property.modified) {
                        map[property.key] = property.value
                    }
                }
            }

            // 不要であれば何もしない
            if (commitValues.isEmpty()) {
                return
            }

            // 保存する
            TextKeyValueStore(context, databasePath).open(TextKeyValueStore.OPEN_WRITABLE).use { kvs ->
                kvs.transaction { _ ->
                    commitValues.forEach { entry ->
                        kvs.put(entry.key, entry.value)

                    }
                }

                // transaction OK!
                propMap.forEach { entry ->
                    entry.value.modified = false
                }
            }
        }
    }

    /**
     * Load properties from database.
     * If in-memory-value is newer than database-value, Preferentially use to database-value.
     */
    override fun load() {
        lock.withLock {
            TextKeyValueStore(context, databasePath).open(TextKeyValueStore.OPEN_READ_ONLY).use { kvs ->
                // All value replace to database-value.
                for ((_, value) in propMap) {
                    value.value = kvs.get(value.key).let { it?.value ?: value.defaultValue }
                    value.modified = false
                }
            }
        }
    }
}
