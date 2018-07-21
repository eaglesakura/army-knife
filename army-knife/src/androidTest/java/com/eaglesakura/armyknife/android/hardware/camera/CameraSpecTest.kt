package com.eaglesakura.armyknife.android.hardware.camera

import android.os.Build
import com.eaglesakura.KtxTestCase
import com.eaglesakura.armyknife.android.hardware.camera.spec.CameraType
import com.eaglesakura.armyknife.junit.blockingTest
import com.eaglesakura.armyknife.junit.validate
import org.junit.Assert.*
import org.junit.Test

class CameraSpecTest : KtxTestCase() {

    @Test
    fun getSpecs() = blockingTest {
        val specs = CameraSpec.getSpecs(application, CameraApi.Default, CameraType.Back)

        assertEquals(CameraType.Back, specs.type)
        specs.getJpegPictureSize(640, 480).also {
            it.width.validate {
                from(1)
                to(640)
            }
            it.height.validate {
                from(1)
                to(480)
            }
        }
    }

    @Test
    fun connectAndDisconnect() = blockingTest {
        val specs = CameraSpec.getSpecs(application, CameraApi.Default, CameraType.Back)
        val controlManager = CameraControlManager.newInstance(application, CameraApi.Default, CameraConnectRequest(CameraType.Back))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            assertTrue(controlManager is Camera2ControlManager)
        }
        assertFalse(controlManager.connected)

        controlManager.connect(
                previewSurface = null,
                previewRequest = null,
                shotRequest = CameraPictureShotRequest(specs.fullJpegPictureSize))
        try {
            assertTrue(controlManager.connected)
        } finally {
            controlManager.disconnect()
        }
    }

    @Test
    fun startPreview() = blockingTest {
        val specs = CameraSpec.getSpecs(application, CameraApi.Default, CameraType.Back)
        val controlManager = CameraControlManager.newInstance(application, CameraApi.Default, CameraConnectRequest(CameraType.Back))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            assertTrue(controlManager is Camera2ControlManager)
        }
        assertFalse(controlManager.connected)

        controlManager.connect(
                previewSurface = null,
                previewRequest = CameraPreviewRequest(specs.minimumPreviewSize),
                shotRequest = CameraPictureShotRequest(specs.fullJpegPictureSize))
        try {
            assertTrue(controlManager.connected)
        } finally {
            controlManager.disconnect()
        }
    }
}