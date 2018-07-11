package com.eaglesakura.ktx.android.extensions

import android.content.ContentValues
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteTransactionListener
import android.os.Build
import android.os.CancellationSignal
import android.util.Pair
import androidx.annotation.RequiresApi
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteQuery
import androidx.sqlite.db.SupportSQLiteStatement
import java.io.IOException
import java.util.*

/**
 * Cursor wrapper function for SupportSQLiteDatabase.
 * Example, Force cancel on "cursor.next()".
 */
typealias CursorWrapper = (cursor: Cursor, signal: CancellationSignal?) -> Cursor

fun SQLiteDatabase.asSupport(cursorWrapper: CursorWrapper? = null): SupportSQLiteDatabase {
    return SupportSQLiteDatabaseImpl(this).also {
        if (cursorWrapper != null) {
            it.cursorWrapper = cursorWrapper
        }
    }
}

private class SupportSQLiteDatabaseImpl(private val database: SQLiteDatabase) : SupportSQLiteDatabase {

    var cursorWrapper: (cursor: Cursor, signal: CancellationSignal?) -> Cursor = { cursor, _ -> cursor }

    override fun compileStatement(sql: String): SupportSQLiteStatement {
        return database.compileStatement(sql).asSupport()
    }

    override fun beginTransaction() {
        database.beginTransaction()
    }

    override fun beginTransactionNonExclusive() {
        database.beginTransactionNonExclusive()
    }

    override fun beginTransactionWithListener(transactionListener: SQLiteTransactionListener) {
        database.beginTransactionWithListener(object : android.database.sqlite.SQLiteTransactionListener {
            override fun onBegin() {
                transactionListener.onBegin()
            }

            override fun onCommit() {
                transactionListener.onCommit()
            }

            override fun onRollback() {
                transactionListener.onRollback()
            }
        })
    }

    override fun beginTransactionWithListenerNonExclusive(transactionListener: SQLiteTransactionListener) {
        database.beginTransactionWithListenerNonExclusive(object : android.database.sqlite.SQLiteTransactionListener {
            override fun onBegin() {
                transactionListener.onBegin()
            }

            override fun onCommit() {
                transactionListener.onCommit()
            }

            override fun onRollback() {
                transactionListener.onRollback()
            }
        })
    }

    override fun endTransaction() {
        database.endTransaction()
    }

    override fun setTransactionSuccessful() {
        database.setTransactionSuccessful()
    }

    override fun inTransaction(): Boolean {
        return database.inTransaction()
    }

    override fun isDbLockedByCurrentThread(): Boolean {
        return database.isDbLockedByCurrentThread
    }

    override fun yieldIfContendedSafely(): Boolean {
        return database.yieldIfContendedSafely()
    }

    override fun yieldIfContendedSafely(sleepAfterYieldDelay: Long): Boolean {
        return database.yieldIfContendedSafely(sleepAfterYieldDelay)
    }

    override fun getVersion(): Int {
        return database.version
    }

    override fun setVersion(version: Int) {
        database.version = version
    }

    override fun getMaximumSize(): Long {
        return database.maximumSize
    }

    override fun setMaximumSize(numBytes: Long): Long {
        return database.setMaximumSize(numBytes)
    }

    override fun getPageSize(): Long {
        return database.pageSize
    }

    override fun setPageSize(numBytes: Long) {
        database.pageSize = numBytes
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun query(query: String): Cursor {
        return query(SimpleSQLiteQuery(query))
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun query(query: String, bindArgs: Array<Any>): Cursor {
        return query(SimpleSQLiteQuery(query, bindArgs))
    }


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun query(query: SupportSQLiteQuery): Cursor {
        return query(query, null)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun query(supportQuery: SupportSQLiteQuery, cancellationSignal: CancellationSignal?): Cursor {
        return database.rawQuery(supportQuery.sql, supportQuery.getBindArgs(), cancellationSignal).let {
            return@let cursorWrapper(it, cancellationSignal)
        }
    }

    @Throws(SQLException::class)
    override fun insert(table: String, conflictAlgorithm: Int, values: ContentValues): Long {
        return database.insertWithOnConflict(table, null, values,
                conflictAlgorithm)
    }

    override fun delete(table: String, whereClause: String, whereArgs: Array<Any>): Int {
        val query = ("DELETE FROM " + table
                + if (isEmpty(whereClause)) "" else " WHERE $whereClause")
        val statement = compileStatement(query)
        SimpleSQLiteQuery.bind(statement, whereArgs)
        return statement.executeUpdateDelete()
    }

    override fun update(table: String, conflictAlgorithm: Int, values: ContentValues?, whereClause: String, whereArgs: Array<Any>?): Int {
        // taken from SQLiteDatabase class.
        if (values == null || values.size() == 0) {
            throw IllegalArgumentException("Empty values")
        }
        val sql = StringBuilder(120)
        sql.append("UPDATE ")
        sql.append(CONFLICT_VALUES[conflictAlgorithm])
        sql.append(table)
        sql.append(" SET ")

        // move all bind args to one array
        val setValuesSize = values.size()
        val bindArgsSize = if (whereArgs == null) setValuesSize else setValuesSize + whereArgs.size
        val bindArgs = arrayOfNulls<Any>(bindArgsSize)
        var i = 0
        for (colName in values.keySet()) {
            sql.append(if (i > 0) "," else "")
            sql.append(colName)
            bindArgs[i++] = values.get(colName)
            sql.append("=?")
        }
        if (whereArgs != null) {
            i = setValuesSize
            while (i < bindArgsSize) {
                bindArgs[i] = whereArgs[i - setValuesSize]
                i++
            }
        }
        if (!isEmpty(whereClause)) {
            sql.append(" WHERE ")
            sql.append(whereClause)
        }
        val stmt = compileStatement(sql.toString())
        SimpleSQLiteQuery.bind(stmt, bindArgs)
        return stmt.executeUpdateDelete()
    }

    @Throws(SQLException::class)
    override fun execSQL(sql: String) {
        database.execSQL(sql)
    }

    @Throws(SQLException::class)
    override fun execSQL(sql: String, bindArgs: Array<Any>) {
        database.execSQL(sql, bindArgs)
    }

    override fun isReadOnly(): Boolean {
        return database.isReadOnly
    }

    override fun isOpen(): Boolean {
        return database.isOpen
    }

    override fun needUpgrade(newVersion: Int): Boolean {
        return database.needUpgrade(newVersion)
    }

    override fun getPath(): String {
        return database.path
    }

    override fun setLocale(locale: Locale) {
        database.setLocale(locale)
    }

    override fun setMaxSqlCacheSize(cacheSize: Int) {
        database.setMaxSqlCacheSize(cacheSize)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun setForeignKeyConstraintsEnabled(enable: Boolean) {
        database.setForeignKeyConstraintsEnabled(enable)
    }

    override fun enableWriteAheadLogging(): Boolean {
        return database.enableWriteAheadLogging()
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun disableWriteAheadLogging() {
        database.disableWriteAheadLogging()
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun isWriteAheadLoggingEnabled(): Boolean {
        return database.isWriteAheadLoggingEnabled()
    }

    override fun getAttachedDbs(): MutableList<Pair<String, String>>? {
        return database.attachedDbs
    }

    override fun isDatabaseIntegrityOk(): Boolean {
        return database.isDatabaseIntegrityOk
    }

    @Throws(IOException::class)
    override fun close() {
        database.close()
    }

    companion object {

        private val CONFLICT_VALUES = arrayOf("", " OR ROLLBACK ", " OR ABORT ", " OR FAIL ", " OR IGNORE ", " OR REPLACE ")
        private val EMPTY_STRING_ARRAY = arrayOfNulls<String>(0)

        private fun isEmpty(input: String?): Boolean {
            return input == null || input.isEmpty()
        }
    }
}