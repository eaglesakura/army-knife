package com.eaglesakura.ktx.android.extensions

import android.database.sqlite.SQLiteOpenHelper
import android.os.Build.VERSION_CODES.JELLY_BEAN
import androidx.annotation.RequiresApi
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper

fun SQLiteOpenHelper.asSupport(): SupportSQLiteOpenHelper = SupportSQLiteOpenHelperImpl(this)

private class SupportSQLiteOpenHelperImpl(private val helper: SQLiteOpenHelper) : SupportSQLiteOpenHelper {
    override fun getDatabaseName(): String = helper.databaseName

    override fun getWritableDatabase(): SupportSQLiteDatabase = helper.writableDatabase.asSupport()

    override fun getReadableDatabase(): SupportSQLiteDatabase = helper.readableDatabase.asSupport()

    override fun close() = helper.close()

    @RequiresApi(JELLY_BEAN)
    override fun setWriteAheadLoggingEnabled(enabled: Boolean) {
        helper.setWriteAheadLoggingEnabled(enabled)
    }
}