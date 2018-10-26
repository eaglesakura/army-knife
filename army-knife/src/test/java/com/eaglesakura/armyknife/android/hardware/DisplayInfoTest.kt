package com.eaglesakura.armyknife.android.hardware

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eaglesakura.armyknife.android.junit4.extensions.instrumentationTest
import com.eaglesakura.armyknife.android.junit4.extensions.targetContext
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DisplayInfoTest {

    @Test
    fun displayInfoRead() = instrumentationTest {
        val displayInfo = DisplayInfo.newInstance(targetContext)
        Log.d(javaClass.simpleName, "$displayInfo")

        assertNotEquals(0, displayInfo.widthPixel)
        assertNotEquals(0, displayInfo.heightPixel)
        assertNotEquals(displayInfo.widthPixel, displayInfo.heightPixel)

        assertNotEquals(0, displayInfo.widthDp)
        assertNotEquals(0, displayInfo.heightDp)
        assertNotEquals(displayInfo.widthDp, displayInfo.heightDp)

        assertNotEquals(0, displayInfo.diagonalInch)
        assertNotEquals(0, displayInfo.diagonalRoundInch.major)
    }
}