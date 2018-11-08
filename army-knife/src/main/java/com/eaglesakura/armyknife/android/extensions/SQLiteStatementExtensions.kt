package com.eaglesakura.armyknife.android.extensions

import android.database.sqlite.SQLiteStatement
import androidx.sqlite.db.SupportSQLiteStatement

/**
 * SQLiteStatement to SupportSQLiteStatement.
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
fun SQLiteStatement.asSupport(): SupportSQLiteStatement = SupportSqliteStatementImpl(this)

private class SupportSqliteStatementImpl(private val mStatement: SQLiteStatement) :
    SupportSQLiteStatement {
    override fun execute() {
        mStatement.execute()
    }

    override fun executeUpdateDelete(): Int {
        return mStatement.executeUpdateDelete()
    }

    override fun executeInsert(): Long {
        return mStatement.executeInsert()
    }

    override fun simpleQueryForLong(): Long {
        return mStatement.simpleQueryForLong()
    }

    override fun simpleQueryForString(): String {
        return mStatement.simpleQueryForString()
    }

    override fun bindNull(index: Int) {
        mStatement.bindNull(index)
    }

    override fun bindLong(index: Int, value: Long) {
        mStatement.bindLong(index, value)
    }

    override fun bindDouble(index: Int, value: Double) {
        mStatement.bindDouble(index, value)
    }

    override fun bindString(index: Int, value: String) {
        mStatement.bindString(index, value)
    }

    override fun bindBlob(index: Int, value: ByteArray) {
        mStatement.bindBlob(index, value)
    }

    override fun clearBindings() {
        mStatement.clearBindings()
    }

    @Throws(Exception::class)
    override fun close() {
        mStatement.close()
    }
}