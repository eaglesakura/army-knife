package com.eaglesakura.armyknife.android.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

internal const val DATABASE_VERSION = 0x01
internal const val TABLE_NAME = "TEXT_KVS"

/**
 * Raw key
 */
internal const val COLUMN_KEY = "KEY"

/**
 * Raw Value
 */
internal const val COLUMN_VALUE = "VALUE"

/**
 * Insert date
 */
internal const val COLUMN_DATE = "DATE"

internal class KeyValueStoreHelper(context: Context, path: String?) : SQLiteOpenHelper(context, path, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE)
        db.execSQL(SQL_GENERATE_INDEX)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_TABLE)
        db.execSQL(SQL_CREATE_TABLE)
        db.execSQL(SQL_GENERATE_INDEX)
    }

    private val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    private val SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME" +
            "(" +
            "$COLUMN_KEY TEXT PRIMARY KEY NOT NULL UNIQUE, " +
            "$COLUMN_VALUE TEXT NOT NULL, " +
            "$COLUMN_DATE INTEGER NOT NULL" +
            ")"
    private val SQL_GENERATE_INDEX = "CREATE INDEX KEY ON $TABLE_NAME($COLUMN_KEY, $COLUMN_DATE)"

}