package com.eaglesakura.ktx.sloth.bluetooth

import androidx.lifecycle.Observer
import android.bluetooth.le.ScanResult
import com.eaglesakura.KtxTestCase
import com.eaglesakura.ktx.junit.blockingTest
import com.eaglesakura.ktx.runtime.extensions.withTimeout
import com.eaglesakura.ktx.sloth.SlothLog
import com.eaglesakura.oneshotlivedata.newEventObserver
import kotlinx.coroutines.experimental.NonCancellable
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.withContext
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.TimeUnit
import kotlin.coroutines.experimental.coroutineContext

class BleDeviceStreamTest : KtxTestCase() {

    @Test
    fun testBleScan() = blockingTest(UI) {
        val scanner = BleDeviceStream(application)
        Assert.assertTrue(scanner.hasPermissions)

        val dataObserver = Observer<List<ScanResult>> {
            SlothLog.bluetooth("Device num[${it!!.size}]")
            it.forEach {
                SlothLog.bluetooth("  - Device name[${it.device.name}] addr[${it.device.address}]")
            }
        }

        scanner.observeForever(dataObserver)
        try {
            scanner.event.observeForever(newEventObserver { event ->
                when (event) {
                    is BluetoothScanEvent -> {
                        SlothLog.bluetooth("OneshotData [${event.id}] Device[${event.scanResult.device.name}]")
                    }
                }
            })

            // check device
            withTimeout(coroutineContext, 60, TimeUnit.SECONDS) {
                while (isActive) {
                    delay(1000)
                    scanner.value?.size?.also { size ->
                        if (size > 0) {
                            SlothLog.bluetooth("Test completed.")
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