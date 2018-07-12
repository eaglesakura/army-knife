package com.eaglesakura.armyknife.android.extensions

import android.graphics.Rect
import android.view.View


/**
 * This method returns absolute view area of Android device display.
 */
fun View.getScreenArea(): Rect {
    val area = Rect()

    val viewInWindow = IntArray(2)
    val viewOnScreen = IntArray(2)
    val windowOnScreen = IntArray(2)

    getLocationInWindow(viewInWindow)
    getLocationOnScreen(viewOnScreen)
    windowOnScreen[0] = viewOnScreen[0] - viewInWindow[0]
    windowOnScreen[1] = viewOnScreen[1] - viewInWindow[1]

    getGlobalVisibleRect(area)
    area.offset(windowOnScreen[0], windowOnScreen[1])
    return area
}

/**
 * This method returns relative view area of Application window.
 */
fun View.getWindowArea(): Rect {
    val xy = IntArray(2)
    getLocationInWindow(xy)
    return Rect(xy[0], xy[1], xy[0] + width, xy[1] + height)
}