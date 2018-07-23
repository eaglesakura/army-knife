package com.eaglesakura.armyknife.android.hardware.camera.spec

import com.eaglesakura.BaseTestCase
import org.junit.Assert.*
import org.junit.Test

class CaptureSizeTest : BaseTestCase() {

    @Test
    fun getPreviewSizeInWindow() {
        CaptureSize(640, 480).also { captureSize ->
            // just size.
            captureSize.getPreviewSizeInWindow(640, 460).also { previewSize ->
                assertEquals(previewSize.size, 2)
                assertEquals(previewSize[0], 640)
                assertEquals(previewSize[1], 480)
            }
            // long width
            captureSize.getPreviewSizeInWindow(1280, 460).also { previewSize ->
                assertEquals(previewSize.size, 2)
                assertEquals(previewSize[0], 640)
                assertEquals(previewSize[1], 480)
            }
            // short width
            captureSize.getPreviewSizeInWindow(480, 460).also { previewSize ->
                assertEquals(previewSize.size, 2)
                assertEquals(previewSize[0], 480)
                assertEquals(previewSize[1], 360)
            }
            // long height
            captureSize.getPreviewSizeInWindow(640, 920).also { previewSize ->
                assertEquals(previewSize.size, 2)
                assertEquals(previewSize[0], 640)
                assertEquals(previewSize[1], 480)
            }
            // short height
            captureSize.getPreviewSizeInWindow(640, 360).also { previewSize ->
                assertEquals(previewSize.size, 2)
                assertEquals(previewSize[0], 480)
                assertEquals(previewSize[1], 360)
            }
        }
    }
}