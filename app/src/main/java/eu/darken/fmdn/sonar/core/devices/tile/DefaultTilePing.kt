package eu.darken.fmdn.sonar.core.devices.tile

import eu.darken.fmdn.common.asHex
import eu.darken.fmdn.sonar.core.ble.BleScanResult
import eu.darken.fmdn.sonar.core.devices.gfd.GFDTrackerPing

data class DefaultTilePing(
    override val scanResult: BleScanResult,
    override val raw: UByteArray,
) : TileTrackerPing {

    override val quickInfo: String
        get() = "${raw.asHex()}"

    override fun toString(): String {
        return "DefaultTilePing($address, $rssi, $quickInfo)"
    }
}
