package com.eaglesakura.ktx.android.extensions

import android.view.KeyEvent

fun KeyEvent.isBackKeyRelease(): Boolean {
    return action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK
}