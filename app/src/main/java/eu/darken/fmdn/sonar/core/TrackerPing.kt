package eu.darken.fmdn.sonar.core

import android.bluetooth.BluetoothDevice
import eu.darken.fmdn.sonar.core.ble.BleScanResult
import java.time.Instant

interface TrackerPing {
    val scanResult: BleScanResult

    val address: String
        get() = scanResult.address

    val device: BluetoothDevice
        get() = scanResult.scanResult.device

    val rssi: Int
        get() = scanResult.rssi

    val lastSeen: Instant
        get() = scanResult.receivedAt

    val raw: UByteArray

    val quickInfo: String
}