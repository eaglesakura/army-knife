package com.eaglesakura.ktx.android.db

import com.eaglesakura.ktx.BaseTestCase
import com.eaglesakura.ktx.android.logger.Logger
import org.junit.Assert.*
import org.junit.Test

class TextKeyValueStoreTest : BaseTestCase() {

    private val TAG = javaClass.simpleName

    @Test
    fun openReadable() {
        val store = TextKeyValueStore(application, application.getDatabasePath("test.db"))

        store.open(TextKeyValueStore.OPEN_READ_ONLY).use {
            Logger.debug(TAG, "Database path ${it.path!!.absolutePath}")
        }
    }

    @Test
    fun openWritable() {
        val store = TextKeyValueStore(application, application.getDatabasePath("test.db"))

        store.open(TextKeyValueStore.OPEN_WRITABLE).use {
            Logger.debug(TAG, "Database path ${it.path!!.absolutePath}")
        }
    }

    @Test
    fun insertWithoutTransaction() {
        val store = TextKeyValueStore(application, application.getDatabasePath("test.db"))

        store.open(TextKeyValueStore.OPEN_WRITABLE).use {
            it.put("key", "value")
        }

        store.open(TextKeyValueStore.OPEN_READ_ONLY).use {
            store.get("key").let {
                assertNotNull(it!!)
                assertEquals("key", it.key)
                assertEquals("value", it.value)
            }
        }
    }

    @Test
    fun insertInTransaction() {
        val store = TextKeyValueStore(application, application.getDatabasePath("test.db"))

        store.open(TextKeyValueStore.OPEN_WRITABLE).use { kvs ->
            Logger.debug(TAG, "Database path ${kvs.path!!.absolutePath}")
            kvs.transaction { _ ->
                kvs.put("key0", "value0")
                kvs.put("key1", "value1")
            }
        }

        store.open(TextKeyValueStore.OPEN_READ_ONLY).use {
            store.get("key0").let {
                assertNotNull(it!!)
                assertEquals("key0", it.key)
                assertEquals("value0", it.value)
            }
            store.get("key1").let {
                assertNotNull(it!!)
                assertEquals("key1", it.key)
                assertEquals("value1", it.value)
            }
        }
    }

    @Test
    fun replaceInTransaction() {
        val store = TextKeyValueStore(application, application.getDatabasePath("test.db"))

        store.open(TextKeyValueStore.OPEN_WRITABLE).use { kvs ->
            Logger.debug(TAG, "Database path ${kvs.path!!.absolutePath}")

            kvs.transaction { _ ->
                kvs.put("key0", "value0")
                kvs.put("key0", "value1")
            }
        }

        store.open(TextKeyValueStore.OPEN_READ_ONLY).use {
            store.get("key0").let {
                assertNotNull(it!!)
                assertEquals("key0", it.key)
                assertEquals("value1", it.value)
            }
        }
    }

    @Test
    fun replaceWithoutTransaction() {
        val store = TextKeyValueStore(application, application.getDatabasePath("test.db"))

        store.open(TextKeyValueStore.OPEN_WRITABLE).use { kvs ->
            Logger.debug(TAG, "Database path ${kvs.path!!.absolutePath}")

            kvs.put("key0", "value0")
            kvs.put("key0", "value1")
        }

        store.open(TextKeyValueStore.OPEN_READ_ONLY).use {
            store.get("key0").let {
                assertNotNull(it!!)
                assertEquals("key0", it.key)
                assertEquals("value1", it.value)
            }
        }
    }
}