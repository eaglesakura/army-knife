package com.eaglesakura.firearm.bluetooth

import android.bluetooth.le.ScanResult
import androidx.lifecycle.Observer
import com.eaglesakura.AndroidTestCase
import com.eaglesakura.armyknife.android.logger.Logger
import com.eaglesakura.armyknife.junit.blockingTest
import com.eaglesakura.oneshotlivedata.newEventObserver
import kotlinx.coroutines.experimental.NonCancellable
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.withContext
import kotlinx.coroutines.experimental.withTimeout
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.TimeUnit

class BleDeviceStreamTest : AndroidTestCase() {

    @Test
    fun testBleScan() = blockingTest(UI) {
        val scanner = BleDeviceStream(application)
        Assert.assertTrue(scanner.hasPermissions)

        val dataObserver = Observer<List<ScanResult>> {
            Logger.debug("BLE", "Device num[${it!!.size}]")
            it.forEach {
                Logger.debug("BLE", "  - Device name[${it.device.name}] addr[${it.device.address}]")
            }
        }

        scanner.observeForever(dataObserver)
        try {
            scanner.event.observeForever(newEventObserver { event ->
                when (event) {
                    is BluetoothScanEvent -> {
                        Logger.debug("BLE", "OneshotData [${event.id}] Device[${event.scanResult.device.name}]")
                    }
                }
            })

            // check device
            withTimeout(60, TimeUnit.SECONDS) {
                while (isActive) {
                    delay(1000)
                    scanner.value?.size?.also { size ->
                        if (size > 0) {
                            Logger.debug("BLE", "Test completed.")
                            return@withTimeout
                        }
                    }
                }
            }
        } finally {
            withContext(NonCancellable) {
                scanner.removeObserver(dataObserver)
            }
        }
    }
}