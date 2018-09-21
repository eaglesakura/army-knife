package com.eaglesakura.armyknife.android.extensions

import android.os.Bundle
import com.eaglesakura.BaseTestCase
import org.junit.Assert.*
import org.junit.Test

class ParcelableExtensionsKtTest : BaseTestCase() {

    @Test
    fun marshal_by_CREATOR() {
        val bundle = Bundle()
        bundle.putString("key", "value")

        val data = bundle.marshal()
        assertNotNull(data)
        assertNotEquals(0, data.size)

        val unmarshal = Bundle.CREATOR.unmarshal(data)
        assertEquals("value", unmarshal.get("key"))
    }

    @Test
    fun marshal_by_KClass() {
        val bundle = Bundle()
        bundle.putString("key", "value")
        val data = bundle.marshal()
        val unmarshal = Bundle::class.unmarshal(data)
        assertEquals("value", unmarshal.get("key"))
    }

    @Test
    fun deepCopy() {
        val bundle = Bundle()
        bundle.putString("key", "value")

        val unmarshal = bundle.deepCopy()
        assertEquals("value", unmarshal.get("key"))
    }
}