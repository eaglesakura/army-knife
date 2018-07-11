package com.eaglesakura.ktx.sloth.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.eaglesakura.ktx.android.RuntimePermissions
import com.eaglesakura.ktx.sloth.SlothLog
import com.eaglesakura.ktx.oneshotlivedata.EventStream
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

@SuppressLint("MissingPermission")
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class BleDeviceStream(private val context: Context) : LiveData<List<ScanResult>>() {

    /**
     * Device scan events.
     */
    val event = EventStream(BluetoothScanEvent.EVENT_LOST, BluetoothScanEvent.EVENT_FOUND, BluetoothScanEvent.EVENT_UPDATED)

    /**
     * Ble device cache expired time.
     */
    val expireTimeMs: Long = 1000 * 60

    private val caches = mutableListOf<ScanResultCache>()

    private var scanJob: Job? = null

    private val scanner: BluetoothLeScanner

    /**
     * current time function.
     */
    var currentTimeMillis: () -> Long = System::currentTimeMillis

    /**
     * This app has runtime permissions for Bluetooth Scan.
     */
    val hasPermissions: Boolean
        get() {
            if (!RuntimePermissions.hasAllRuntimePermissions(context, RuntimePermissions.PERMISSIONS_BLUETOOTH)) {
                return false
            }

            if (!RuntimePermissions.hasAllRuntimePermissions(context, listOf(Manifest.permission.ACCESS_FINE_LOCATION))
                    && !RuntimePermissions.hasAllRuntimePermissions(context, listOf(Manifest.permission.ACCESS_COARSE_LOCATION))) {
                return false
            }

            return true
        }

    init {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        scanner = bluetoothManager.adapter.bluetoothLeScanner
    }

    override fun onActive() {
        super.onActive()
        scanner.startScan(scanCallback)
        startCacheCleanAsync()
    }

    override fun onInactive() {
        scanJob?.cancel()
        scanJob = null
        scanner.stopScan(scanCallback)
        super.onInactive()
    }

    override fun getValue(): List<ScanResult>? {
        cleanUp()
        return super.getValue()
    }

    private fun refreshData() {
        // remake data.
        val list = mutableListOf<ScanResult>()
        caches.forEach {
            list.add(it.scanResult)
        }
        value = list
    }

    private fun cleanUp() {
        var dirty = false
        caches.iterator().let { iterator ->
            while (iterator.hasNext()) {
                val cache = iterator.next()
                if (cache.expired) {
                    dirty = true
                    iterator.remove()
                    event.setOneshot(BluetoothScanEvent(id = BluetoothScanEvent.EVENT_LOST, scanResult = cache.scanResult))
                }
            }
        }

        if (dirty) {
            refreshData()
        }
    }

    private fun startCacheCleanAsync() {
        scanJob = launch {
            try {
                while (isActive) {
                    delay(1000 * 5)
                    async(UI) { cleanUp() }.await()
                }
            } finally {
                SlothLog.bluetooth("Scan clean up abort.")
            }
        }
    }

    private var scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            try {
                caches.forEach {
                    if (it.scanResult.device.address == result.device.address) {
                        SlothLog.bluetooth("Ble update device name[${result.device.name}]")
                        it.scanResult = result
                        event.setOneshot(BluetoothScanEvent(id = BluetoothScanEvent.EVENT_UPDATED, scanResult = result))
                        return
                    }
                }

                // new data
                SlothLog.bluetooth("Ble new device found name[${result.device.name}]")
                event.setOneshot(BluetoothScanEvent(id = BluetoothScanEvent.EVENT_FOUND, scanResult = result))
                caches.add(ScanResultCache(result))
            } finally {
                cleanUp()
                refreshData()
            }
        }

        override fun onScanFailed(errorCode: Int) {
            SlothLog.bluetooth("Ble scan failed[$errorCode]")
        }
    }

    internal inner class ScanResultCache(scanResult: ScanResult) {
        var modifiedTime: Long = currentTimeMillis()

        var scanResult = scanResult
            set(value) {
                field = value
                modifiedTime = currentTimeMillis()
            }

        /**
         * This object expired now.
         */
        val expired: Boolean
            get() = currentTimeMillis() > (modifiedTime + expireTimeMs)
    }
}