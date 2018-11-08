package com.eaglesakura.armyknife.android.extensions

import android.database.sqlite.SQLiteProgram
import androidx.sqlite.db.SupportSQLiteProgram


/**
 * SQLiteProgram to SupportSQLiteProgram.
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
fun SQLiteProgram.asSupport(): SupportSQLiteProgram = SupportSQLiteProgramImpl(this)

private class SupportSQLiteProgramImpl(private val mProgram: SQLiteProgram) : SupportSQLiteProgram {

    override fun bindNull(index: Int) {
        mProgram.bindNull(index)
    }

    override fun bindLong(index: Int, value: Long) {
        mProgram.bindLong(index, value)
    }

    override fun bindDouble(index: Int, value: Double) {
        mProgram.bindDouble(index, value)
    }

    override fun bindString(index: Int, value: String) {
        mProgram.bindString(index, value)
    }

    override fun bindBlob(index: Int, value: ByteArray) {
        mProgram.bindBlob(index, value)
    }

    override fun clearBindings() {
        mProgram.clearBindings()
    }

    @Throws(Exception::class)
    override fun close() {
        mProgram.close()
    }
}