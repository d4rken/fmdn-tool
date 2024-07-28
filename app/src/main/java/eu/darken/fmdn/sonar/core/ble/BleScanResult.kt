package eu.darken.fmdn.sonar.core.ble

import android.bluetooth.le.ScanResult
import android.os.Parcelable
import eu.darken.fmdn.common.asHex
import kotlinx.parcelize.Parcelize
import java.time.Instant

@Parcelize
data class BleScanResult(
    val receivedAt: Instant,
    val scanResult: ScanResult,
) : Parcelable {
    val address: String
        get() = scanResult.device.address

    val rssi: Int
        get() = scanResult.rssi

    val raw: UByteArray
        get() = scanResult.scanRecord!!.bytes.asUByteArray()

    override fun toString(): String {
        return "BleScanResult($rssi, $address, ${raw.asHex()})"
    }

    companion object {
        fun fromScanResult(scanResult: ScanResult) = BleScanResult(
            receivedAt = Instant.now(),
            scanResult = scanResult,
        )
    }
}