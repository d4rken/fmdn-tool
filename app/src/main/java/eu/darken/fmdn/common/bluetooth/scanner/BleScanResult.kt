package eu.darken.fmdn.common.bluetooth.scanner

import android.bluetooth.le.ScanResult
import android.os.Parcelable
import eu.darken.fmdn.common.asHumanReadableHex
import kotlinx.parcelize.Parcelize
import okio.ByteString
import okio.ByteString.Companion.toByteString
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

    val raw: ByteString?
        get() = scanResult.scanRecord!!.bytes?.toByteString()

    override fun toString(): String {
        return "BleScanResult($rssi, $address, ${raw?.asHumanReadableHex()})"
    }

    companion object {
        fun fromScanResult(scanResult: ScanResult) = BleScanResult(
            receivedAt = Instant.now(),
            scanResult = scanResult,
        )
    }
}