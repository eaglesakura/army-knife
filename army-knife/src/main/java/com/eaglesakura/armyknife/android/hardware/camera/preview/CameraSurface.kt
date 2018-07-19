package com.eaglesakura.armyknife.android.hardware.camera.preview

import android.graphics.SurfaceTexture
import android.os.Build
import android.view.Surface

import com.eaglesakura.armyknife.android.hardware.camera.spec.CaptureSize


class CameraSurface {
    private var mNativeSurface: Surface? = null

    private var mSurfaceTexture: SurfaceTexture? = null

    constructor(surfaceTexture: SurfaceTexture) {
        mSurfaceTexture = surfaceTexture
    }

    constructor(nativeSurface: Surface) {
        mNativeSurface = nativeSurface
    }

    fun getNativeSurface(previewSize: CaptureSize): Surface {
        mSurfaceTexture?.also { surfaceTexture ->
            if (Build.VERSION.SDK_INT >= 15) {
                surfaceTexture.setDefaultBufferSize(previewSize.width, previewSize.height)
            }

            if (mNativeSurface == null) {
                mNativeSurface = Surface(mSurfaceTexture)
            }
        }

        if (mNativeSurface == null) {
            throw NullPointerException("mNativeSurface == null")
        }

        return mNativeSurface!!
    }


    fun getSurfaceTexture(previewSize: CaptureSize): SurfaceTexture {
        mSurfaceTexture?.also { surfaceTexture ->
            if (Build.VERSION.SDK_INT >= 15) {
                surfaceTexture.setDefaultBufferSize(previewSize.width, previewSize.height)
            }
        }
        return mSurfaceTexture!!
    }
}
