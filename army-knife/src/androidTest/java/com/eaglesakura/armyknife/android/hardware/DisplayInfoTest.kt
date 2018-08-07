package com.eaglesakura.armyknife.android.hardware

import com.eaglesakura.KtxTestCase
import org.junit.Assert.assertNotEquals
import org.junit.Test

class DisplayInfoTest : KtxTestCase() {

    @Test
    fun displayInfoRead() {
        val displayInfo = DisplayInfo.newInstance(application)
        console("$displayInfo")

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