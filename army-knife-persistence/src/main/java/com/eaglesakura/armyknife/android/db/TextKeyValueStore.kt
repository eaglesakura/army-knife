package com.eaglesakura.armyknife.android.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteQueryBuilder
import androidx.sqlite.db.transaction
import com.eaglesakura.armyknife.android.extensions.asSupport
import java.io.Closeable
import java.io.File
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

typealias DatabaseOpenFunction = (context: Context, filePath: String?) -> SupportSQLiteDatabase

/**
 * TextKeyValueStore is A simple Key-Value store for Android app.
 * This class use SQLite. Not used third-party class libraries.
 *
 * When "path" argument set to null, SQLite use in-memory database.
 * Columns
 * [key:Text][value:Text][date:Integer]
 */
class TextKeyValueStore(private val context: Context, internal val path: File?) : Closeable {
    private var db: SupportSQLiteDatabase? = null

    private var refs = AtomicInteger()

    private fun validDatabase(): SupportSQLiteDatabase = db
            ?: throw IllegalStateException("Database not open.")

    /**
     * Target file path to database path.
     * If this function returns null, The SQLite create database in memory.
     */
    var fileToPath: (file: File?) -> String? = { file ->
        file?.absolutePath?.let { fullPath ->
            return@let if (fullPath[1] == ':') {
                // This is windows.
                file.name
            } else {
                fullPath
            }
        }
    }

    /**
     * Get current time function.
     * Returns the current time in milliseconds.
     */
    var getTime: () -> Long = { System.currentTimeMillis() }

    /**
     * Do action() in transaction.
     */
    @Suppress("unused")
    fun <R> transaction(action: (db: SupportSQLiteDatabase) -> R): R {
        return validDatabase().transaction {
            action(this)
        }
    }

    /**
     * Insert key-value data to database.
     * If database has old data, Replace to new data.
     */
    @Suppress("unused")
    fun put(key: String, value: String) {
        val row = ContentValues(3)
        row.put(COLUMN_KEY, key)
        row.put(COLUMN_VALUE, value)
        row.put(COLUMN_DATE, getTime())

        validDatabase().insert(TABLE_NAME, SQLiteDatabase.CONFLICT_REPLACE, row)
    }

    /**
     * Get key-value data from database.
     * If key not found, this method returns null.
     */
    fun get(key: String): KeyValueData? {
        val query = SupportSQLiteQueryBuilder.builder(TABLE_NAME)
                .selection("$COLUMN_KEY=?", arrayOf(key))
                .columns(arrayOf(COLUMN_VALUE, COLUMN_DATE))
                .create()

        validDatabase().query(query).use { cursor ->
            return if (!cursor.moveToFirst()) {
                null
            } else {
                val value = cursor.getString(0)
                val date = cursor.getLong(1)
                KeyValueData(key, value, Date(date))
            }
        }
    }

    /**
     * Open this database.
     *
     * @see OPEN_READ_ONLY
     * @see OPEN_WRITABLE
     */
    @Suppress("unused")
    fun open(function: DatabaseOpenFunction): TextKeyValueStore {
        if (refs.incrementAndGet() != 1) {
            return this
        }


        db = function(context, fileToPath(path))
        return this
    }

    override fun close() {
        refs.decrementAndGet().let {
            if (it > 0) {
                return
            }
            if (it < 0) {
                throw IllegalStateException("Close error.")
            }
        }

        db?.close()
        db = null
    }

    companion object {
        /**
         * Open key-value store for read-only.
         */
        val OPEN_READ_ONLY: DatabaseOpenFunction = { context, filePath ->
            KeyValueStoreHelper(context, filePath).readableDatabase.asSupport()
        }

        /**
         * Open key-value store for write.
         */
        val OPEN_WRITABLE: DatabaseOpenFunction = { context, filePath ->
            KeyValueStoreHelper(context, filePath).writableDatabase.asSupport()
        }
    }
}