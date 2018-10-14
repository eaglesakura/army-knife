package com.eaglesakura.firearm.bluetooth

import android.bluetooth.le.ScanResult
import androidx.lifecycle.Observer
import com.eaglesakura.AndroidTestCase
import com.eaglesakura.armyknife.android.logger.Logger
import com.eaglesakura.armyknife.junit.blockingTest
import kotlinx.coroutines.*
import kotlinx.coroutines.android.Main
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.TimeUnit

class BleDeviceStreamTest : AndroidTestCase() {

    @Test
    fun testBleScan() = blockingTest(Dispatchers.Main) {
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
            scanner.event.subscribe { event ->
                when (event) {
                    is BluetoothScanEvent -> {
                        Logger.debug("BLE", "OneshotData [${event.id}] Device[${event.scanResult.device.name}]")
                    }
                }
            }

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