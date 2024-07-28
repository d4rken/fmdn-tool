package eu.darken.fmdn.sonar.core.devices.samsung

import eu.darken.fmdn.common.asHex
import eu.darken.fmdn.sonar.core.ble.BleScanResult
import eu.darken.fmdn.sonar.core.devices.tile.TileTrackerPing

data class DefaultSamsungPing(
    override val scanResult: BleScanResult,
    override val raw: UByteArray,
) : SamsungTrackerPing {

    override val quickInfo: String
        get() = "${raw.asHex()}"

    override fun toString(): String {
        return "DefaultSamsungPing($address, $rssi, $quickInfo)"
    }
}
