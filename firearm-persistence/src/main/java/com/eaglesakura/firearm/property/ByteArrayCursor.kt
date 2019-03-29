package com.eaglesakura.firearm.property

import android.database.AbstractCursor
import android.database.Cursor
import com.eaglesakura.armyknife.runtime.extensions.decodeBase64
import com.eaglesakura.armyknife.runtime.extensions.encodeBase64
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.Arrays

/**
 * Cursorに乗せて送信する
 *
 * ByteArrayのみサポートされており、その他のサポートは動作が保証されない。
 */
internal class ByteArrayCursor(buffer: ByteArray) : AbstractCursor() {
    private val serializedData = ArrayList<ByteArray>()

    private var cursorIndex = -1

    init {
        //        SlothLog.INSTANCE.cursor(StringUtil.format("ByteArrayCursor[%d bytes]", buffer.length));
        if (buffer.isEmpty()) {
            // 空配列を挿入する
            serializedData.add(ByteArray(0))
        } else {
            var offset = 0
            var length = buffer.size
            while (length > 0) {
                val range = Math.min(1024 * 256, length) // 適当な大きさに分解する
                serializedData.add(Arrays.copyOfRange(buffer, offset, offset + range))

                offset += range
                length -= range
            }
        }
    }

    override fun getCount(): Int {
        //        SlothLog.INSTANCE.cursor(StringUtil.format("ByteArrayCursor.getCount[%d]", serializedData.size()));
        return serializedData.size
    }

    override fun getColumnNames(): Array<String> {
        return arrayOf(COLUMN_NAME_VALUE_FRAGMENT)
    }

    override fun onMove(oldPosition: Int, newPosition: Int): Boolean {
        //        SlothLog.INSTANCE.cursor(StringUtil.format("ByteArrayCursor.onMove old[%d] new[%d]", oldPosition, newPosition));
        cursorIndex = newPosition
        return super.onMove(oldPosition, newPosition)
    }

    override fun getBlob(column: Int): ByteArray? {
        //        SlothLog.INSTANCE.cursor(StringUtil.format("ByteArrayCursor.getString column[%d], %d bytes", column, serializedData.get(cursorIndex).length));
        return null
    }

    override fun getString(column: Int): String {
        //        FwLog.system("ByteArrayCursor.getString column[%d], %d bytes", column, serializedData.get(cursorIndex).length);
        return serializedData[cursorIndex].encodeBase64()
    }

    override fun getShort(i: Int): Short {
        return 0
    }

    override fun getInt(i: Int): Int {
        return 0
    }

    override fun getLong(i: Int): Long {
        return 0
    }

    override fun getFloat(i: Int): Float {
        return 0f
    }

    override fun getDouble(i: Int): Double {
        return 0.0
    }

    override fun isNull(i: Int): Boolean {
        return false
    }

    companion object {
        private const val COLUMN_NAME_VALUE_FRAGMENT = "values"

        /**
         * 分割して送られてきたデータを連結する
         *
         * @param cursor 連結するカーソル
         * @return 連結されたデータ
         */
        fun toByteArray(cursor: Cursor): ByteArray {
            val os = ByteArrayOutputStream(1024)
            if (!cursor.moveToFirst()) {
                throw IllegalStateException()
            }
            //
            try {
                do {
                    val blob = cursor.getString(0).decodeBase64()
                    os.write(blob)
                } while (cursor.moveToNext())

                return os.toByteArray()
            } catch (e: IOException) {
                throw IllegalStateException(e)
            }
        }
    }
}
