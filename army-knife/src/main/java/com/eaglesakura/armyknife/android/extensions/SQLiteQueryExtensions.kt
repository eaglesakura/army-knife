package com.eaglesakura.armyknife.android.extensions

import androidx.sqlite.db.SupportSQLiteQuery
import androidx.sqlite.db.SupportSQLiteStatement
import com.eaglesakura.armyknife.runtime.extensions.encodeBase64

fun SupportSQLiteQuery.getBindArgs(): Array<String?> {
    val args = mutableListOf<String?>()
    this.bindTo(object : SupportSQLiteStatement {
        override fun bindLong(index: Int, value: Long) {
            args += "$value"
        }

        override fun simpleQueryForLong(): Long = 0

        override fun bindString(index: Int, value: String?) {
            args += "$value"
        }

        override fun bindDouble(index: Int, value: Double) {
            args += "$value"
        }

        override fun simpleQueryForString(): String = ""

        override fun clearBindings() = Unit

        override fun execute() = Unit

        override fun executeInsert(): Long = 0

        override fun bindBlob(index: Int, value: ByteArray?) {
            args += value?.encodeBase64()
        }

        override fun executeUpdateDelete(): Int = 0

        override fun close() = Unit

        override fun bindNull(index: Int) {
            args.add(null)
        }
    })
    return args.toTypedArray()
}
