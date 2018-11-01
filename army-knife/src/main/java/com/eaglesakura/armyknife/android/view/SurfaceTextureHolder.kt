package com.eaglesakura.armyknife.android.view

import android.view.SurfaceHolder
import java.io.Closeable

interface SurfaceTextureHolder : SurfaceHolder, Closeable {
    fun onResizeTexture(width: Int, height: Int)
}