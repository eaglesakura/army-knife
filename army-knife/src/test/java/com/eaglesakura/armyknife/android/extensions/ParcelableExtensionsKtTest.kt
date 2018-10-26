package com.eaglesakura.armyknife.android.extensions

import android.os.Bundle
import android.os.Parcelable
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eaglesakura.armyknife.android.junit4.extensions.compatibleTest
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ParcelableExtensionsKtTest {

    @Test
    fun marshal_by_CREATOR() = compatibleTest {
        val bundle = Bundle()
        bundle.putString("key", "value")

        val data = bundle.marshal()
        assertNotNull(data)
        assertNotEquals(0, data.size)

        val unmarshal = Bundle.CREATOR.unmarshal(data)
        assertEquals("value", unmarshal.get("key"))
    }

    @Test
    fun marshal_by_KClass() = compatibleTest {
        val bundle = Bundle()
        bundle.putString("key", "value")
        val data = bundle.marshal()
        val unmarshal = Bundle::class.unmarshal(data)
        assertEquals("value", unmarshal.get("key"))
    }

    @Test
    fun deepCopy() = compatibleTest {
        val bundle = Bundle()
        bundle.putString("key", "value")

        val unmarshal = (bundle as Parcelable).deepCopy<Bundle>()
        assertEquals("value", unmarshal.get("key"))
    }
}