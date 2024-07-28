package eu.darken.fmdn.sonar.core.devices.gfd

import eu.darken.fmdn.common.asHex
import eu.darken.fmdn.sonar.core.ble.BleScanResult

data class DefaultGFDPing(
    override val scanResult: BleScanResult,
    override val raw: UByteArray,
) : GFDTrackerPing {

    override val quickInfo: String
        get() = "$trackingProtectionMode, ${raw.asHex()}"

    override fun toString(): String {
        return "DefaultGFDPing($address, $rssi, $quickInfo)"
    }
}
