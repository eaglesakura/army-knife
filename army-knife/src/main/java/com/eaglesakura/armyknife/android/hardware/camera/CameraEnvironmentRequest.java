package com.eaglesakura.armyknife.android.hardware.camera;

import com.eaglesakura.armyknife.android.hardware.camera.spec.FlashMode;
import com.eaglesakura.armyknife.android.hardware.camera.spec.FocusMode;
import com.eaglesakura.armyknife.android.hardware.camera.spec.Scene;
import com.eaglesakura.armyknife.android.hardware.camera.spec.WhiteBalance;

import androidx.annotation.Nullable;

public final class CameraEnvironmentRequest {

    @Nullable
    FocusMode mFocusMode;

    @Nullable
    Scene mScene;

    @Nullable
    WhiteBalance mWhiteBalance;

    @Nullable
    FlashMode mFlashMode;

    public CameraEnvironmentRequest() {
    }

    public CameraEnvironmentRequest focus(FocusMode focusMode) {
        mFocusMode = focusMode;
        return this;
    }

    public CameraEnvironmentRequest focus(CameraSpec spec, FocusMode focusMode) {
        if (spec.isSupported(focusMode)) {
            mFocusMode = focusMode;
        }
        return this;
    }

    public CameraEnvironmentRequest scene(Scene scene) {
        mScene = scene;
        return this;
    }

    /**
     * 指定したシーンがサポートされていればセットする
     */
    public CameraEnvironmentRequest scene(CameraSpec spec, Scene scene) {
        if (spec.isSupported(scene)) {
            mScene = scene;
        }
        return this;
    }

    public CameraEnvironmentRequest whiteBalance(WhiteBalance whiteBalance) {
        mWhiteBalance = whiteBalance;
        return this;
    }

    public CameraEnvironmentRequest whiteBalance(CameraSpec spec, WhiteBalance whiteBalance) {
        if (spec.isSupported(whiteBalance)) {
            mWhiteBalance = whiteBalance;
        }
        return this;
    }

    public CameraEnvironmentRequest flash(FlashMode flashMode) {
        mFlashMode = flashMode;
        return this;
    }

    public CameraEnvironmentRequest flash(CameraSpec spec, FlashMode flashMode) {
        if (spec.isSupported(flashMode)) {
            mFlashMode = flashMode;
        }
        return this;
    }

    @Nullable
    public FlashMode getFlashMode() {
        return mFlashMode;
    }

    @Nullable
    public FocusMode getFocusMode() {
        return mFocusMode;
    }

    @Nullable
    public Scene getScene() {
        return mScene;
    }

    @Nullable
    public WhiteBalance getWhiteBalance() {
        return mWhiteBalance;
    }
}
