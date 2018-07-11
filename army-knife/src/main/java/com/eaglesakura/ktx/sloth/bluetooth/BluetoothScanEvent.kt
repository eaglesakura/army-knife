package com.eaglesakura.ktx.sloth.bluetooth

import android.bluetooth.le.ScanResult
import com.eaglesakura.oneshotlivedata.Event
import com.eaglesakura.oneshotlivedata.EventId

class BluetoothScanEvent(override val id: EventId, val scanResult: ScanResult) : Event {

    companion object {
        val EVENT_FOUND = EventId("EVENT_FOUND")

        val EVENT_UPDATED = EventId("EVENT_UPDATED")

        val EVENT_LOST = EventId("EVENT_LOST")
    }
}