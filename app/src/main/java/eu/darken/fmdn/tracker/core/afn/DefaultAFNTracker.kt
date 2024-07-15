package eu.darken.fmdn.tracker.core.afn

import eu.darken.fmdn.common.bluetooth.scanner.BleScanResult
import eu.darken.fmdn.tracker.core.Tracker
import okio.ByteString
import java.time.Instant

data class DefaultAFNTracker(
    private val scanResult: BleScanResult,
) : AFNTracker {
    override val id: Tracker.Id
        get() = Tracker.Id(scanResult.address)

    override val lastSeen: Instant
        get() = scanResult.receivedAt

    override val rssi: Int
        get() = scanResult.rssi

    override val raw: ByteString?
        get() = scanResult.raw

    override val address: String
        get() = scanResult.address

    val rawStatus: Byte
}
