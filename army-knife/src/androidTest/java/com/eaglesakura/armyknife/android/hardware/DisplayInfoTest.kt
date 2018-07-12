package com.eaglesakura.armyknife.android.hardware

import android.util.Log
import com.eaglesakura.KtxTestCase
import org.junit.Assert.*
import org.junit.Test

class DisplayInfoTest : KtxTestCase() {

    @Test
    fun displayInfoRead() {
        val displayInfo = DisplayInfo.newInstance(application)
        Log.d(LOG_TAG, "$displayInfo")

        assertNotEquals(0, displayInfo.widthPixel)
        assertNotEquals(0, displayInfo.heightPixel)
        assertNotEquals(displayInfo.widthPixel, displayInfo.heightPixel)

        assertNotEquals(0, displayInfo.widthDp)
        assertNotEquals(0, displayInfo.heightDp)
        assertNotEquals(displayInfo.widthDp, displayInfo.heightDp)

        assertNotEquals(0, displayInfo.diagonalInch)
        assertNotEquals(0, displayInfo.diagonalRoundInch.major)
        assertNotEquals(0, displayInfo.diagonalRoundInch.minor)
    }
}