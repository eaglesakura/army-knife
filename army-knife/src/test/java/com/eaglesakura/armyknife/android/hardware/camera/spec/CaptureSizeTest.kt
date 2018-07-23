package com.eaglesakura.armyknife.android.hardware.camera.spec

import com.eaglesakura.BaseTestCase
import org.junit.Assert.*
import org.junit.Test

class CaptureSizeTest : BaseTestCase() {

    @Test
    fun getPreviewSizeInWindow() {
        CaptureSize(640, 480).also { captureSize ->
            // just size.
            captureSize.getPreviewSizeInWindow(640, 480).also { previewSize ->
                assertEquals(2, previewSize.size)
                assertEquals(640, previewSize[0])
                assertEquals(480, previewSize[1])
            }
            // just with large size.
            captureSize.getPreviewSizeInWindow(640 * 2, 480 * 2).also { previewSize ->
                assertEquals(640 * 2, previewSize[0])
                assertEquals(480 * 2, previewSize[1])
            }
            // just with small size.
            captureSize.getPreviewSizeInWindow(640 / 2, 480 / 2).also { previewSize ->
                assertEquals(640 / 2, previewSize[0])
                assertEquals(480 / 2, previewSize[1])
            }
            // long width
            captureSize.getPreviewSizeInWindow(1280, 480).also { previewSize ->
                assertEquals(640, previewSize[0])
                assertEquals(480, previewSize[1])
            }
            // short width
            captureSize.getPreviewSizeInWindow(480, 480).also { previewSize ->
                assertEquals(480, previewSize[0])
                assertEquals(360, previewSize[1])
            }
            // long height
            captureSize.getPreviewSizeInWindow(640, 960).also { previewSize ->
                assertEquals(640, previewSize[0])
                assertEquals(480, previewSize[1])
            }
            // short height
            captureSize.getPreviewSizeInWindow(640, 360).also { previewSize ->
                assertEquals(480, previewSize[0])
                assertEquals(360, previewSize[1])
            }
        }
    }

    @Test
    fun getPreviewSizeWrapWindow() {
        CaptureSize(640, 480).also { captureSize ->
            // just size.
            captureSize.getPreviewSizeWrapWindow(640, 480).also { previewSize ->
                assertEquals(2, previewSize.size)
                assertEquals(640, previewSize[0])
                assertEquals(480, previewSize[1])
            }
            // just with large size.
            captureSize.getPreviewSizeWrapWindow(640 * 2, 480 * 2).also { previewSize ->
                assertEquals(640 * 2, previewSize[0])
                assertEquals(480 * 2, previewSize[1])
            }
            // just with small size.
            captureSize.getPreviewSizeWrapWindow(640 / 2, 480 / 2).also { previewSize ->
                assertEquals(640 / 2, previewSize[0])
                assertEquals(480 / 2, previewSize[1])
            }
            // long width
            captureSize.getPreviewSizeWrapWindow(1280, 480).also { previewSize ->
                assertEquals(1280, previewSize[0])
                assertEquals(960, previewSize[1])
            }
            // short width
            captureSize.getPreviewSizeWrapWindow(480, 480).also { previewSize ->
                assertEquals(640, previewSize[0])
                assertEquals(480, previewSize[1])
            }
            // long height
            captureSize.getPreviewSizeWrapWindow(640, 960).also { previewSize ->
                assertEquals(1280, previewSize[0])
                assertEquals(960, previewSize[1])
            }
            // short height
            captureSize.getPreviewSizeWrapWindow(640, 360).also { previewSize ->
                assertEquals(640, previewSize[0])
                assertEquals(480, previewSize[1])
            }
        }
    }
}