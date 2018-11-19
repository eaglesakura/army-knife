package com.eaglesakura.armyknife.android

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eaglesakura.armyknife.android.junit4.extensions.instrumentationTest
import com.eaglesakura.armyknife.android.junit4.extensions.localTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ApplicationRuntimeTest {

    @Test
    fun runtimeFlags_instrumentation() = instrumentationTest {
        val flags = ApplicationRuntime.runtimeFlags
        assertEquals(
            ApplicationRuntime.RUNTIME_ART or ApplicationRuntime.RUNTIME_INSTRUMENTATION or ApplicationRuntime.RUNTIME_JUNIT or ApplicationRuntime.RUNTIME_ANDROID_DEVICE,
            flags
        )
    }

    @Test
    fun runtimeFlags_local() = localTest {
        val flags = ApplicationRuntime.runtimeFlags
        assertEquals(
            ApplicationRuntime.RUNTIME_JAVA_VM or ApplicationRuntime.RUNTIME_ROBOLECTRIC or ApplicationRuntime.RUNTIME_JUNIT,
            flags
        )
    }
}