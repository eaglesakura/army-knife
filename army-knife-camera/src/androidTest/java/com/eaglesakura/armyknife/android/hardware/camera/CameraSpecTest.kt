package com.eaglesakura.armyknife.android.hardware.camera

import android.os.Build
import com.eaglesakura.AndroidTestCase
import com.eaglesakura.armyknife.android.hardware.camera.spec.CameraType
import com.eaglesakura.armyknife.android.hardware.camera.spec.FocusMode
import com.eaglesakura.armyknife.android.hardware.camera.spec.Scene
import com.eaglesakura.armyknife.android.hardware.camera.spec.WhiteBalance
import com.eaglesakura.armyknife.junit.blockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.*
import org.junit.Test

class CameraSpecTest : AndroidTestCase() {

    @Test
    fun getSpecs() = blockingTest {
        val specs = CameraSpec.getSpecs(application, CameraApi.Default, CameraType.Back)

        assertEquals(CameraType.Back, specs.type)
        specs.getJpegPictureSize(640, 480).also {
            assertThat(it.width).apply {
                isGreaterThan(0)
                isLessThanOrEqualTo(640)
            }
            assertThat(it.height).apply {
                isGreaterThan(0)
                isLessThanOrEqualTo(480)
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

    @Test
    fun takePicture() = blockingTest {
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

            val picture = controlManager.takePicture(CameraEnvironmentRequest(FocusMode.SETTING_AUTO, Scene.SETTING_AUTO, WhiteBalance.SETTING_AUTO))
            assertTrue(picture.buffer.isNotEmpty())
            assertEquals(specs.fullJpegPictureSize.width, picture.width)
            assertEquals(specs.fullJpegPictureSize.height, picture.height)

            // decode ok
            picture.decodeImage().also { bitmap ->
                assertEquals(Math.max(specs.fullJpegPictureSize.width, specs.fullJpegPictureSize.height), Math.max(bitmap.width, bitmap.height))
                assertEquals(Math.min(specs.fullJpegPictureSize.width, specs.fullJpegPictureSize.height), Math.min(bitmap.width, bitmap.height))
            }
        } finally {
            controlManager.disconnect()
        }
    }
}