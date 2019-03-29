package com.eaglesakura.firearm.bluetooth

import android.bluetooth.le.ScanResult
import android.util.Log
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.RequiresDevice
import com.eaglesakura.armyknife.android.junit4.extensions.compatibleBlockingTest
import com.eaglesakura.armyknife.android.junit4.extensions.targetContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@Ignore
@RunWith(AndroidJUnit4::class)
class BleDeviceStreamTest {

    @RequiresDevice
    @Test
    fun testBleScan() = compatibleBlockingTest(Dispatchers.Main) {
        val scanner = BleDeviceStream(targetContext)
        Assert.assertTrue(scanner.hasPermissions)

        val dataObserver = Observer<List<ScanResult>> {
            Log.d("BLE", "Device num[${it!!.size}]")
            it.forEach {
                Log.d("BLE", "  - Device name[${it.device.name}] addr[${it.device.address}]")
            }
        }

        scanner.observeForever(dataObserver)
        try {
            scanner.event.subscribe { event ->
                when (event) {
                    is BluetoothScanEvent -> {
                        Log.d("BLE", "OneshotData [${event.id}] Device[${event.scanResult.device.name}]")
                    }
                }
            }

            // check device
            withTimeout(TimeUnit.SECONDS.toMillis(60)) {
                while (isActive) {
                    delay(1000)
                    scanner.value?.size?.also { size ->
                        if (size > 0) {
                            Log.d("BLE", "Test completed.")
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