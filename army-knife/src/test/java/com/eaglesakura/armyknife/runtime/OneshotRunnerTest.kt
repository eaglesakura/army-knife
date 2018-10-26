package com.eaglesakura.armyknife.runtime

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eaglesakura.armyknife.android.junit4.extensions.compatibleTest
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OneshotRunnerTest {

    @Test
    fun oneshot() = compatibleTest {
        val runner = OneshotRunner<String>()
        val first = runner.oneshot { Random.largeString() }
        val second = runner.oneshot {
            fail()
            "fail()"
        }
        assertEquals(first, second)
    }
}