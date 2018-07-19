package com.eaglesakura.armyknife.android.hardware.camera.spec

enum class CameraType {
    /**
     * 標準のフロントカメラ
     */
    Front,

    /**
     * 標準のバックカメラ
     */
    Back,

    /**
     * その他接続されているカメラ
     *
     * Camera2のみAPIサポートされているため、正常に選択可能。
     */
    External,

    /**
     * バック => Front => その他のカメラを順に利用する
     */
    Auto
}
