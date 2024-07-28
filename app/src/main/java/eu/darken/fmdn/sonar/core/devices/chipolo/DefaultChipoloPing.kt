package eu.darken.fmdn.sonar.core.devices.chipolo

import eu.darken.fmdn.common.asHex
import eu.darken.fmdn.sonar.core.ble.BleScanResult

data class DefaultChipoloPing(
    override val scanResult: BleScanResult,
    override val raw: UByteArray,
) : ChipoloTrackerPing {

    override val quickInfo: String
        get() = "${raw.asHex()}"

    override fun toString(): String {
        return "DefaultChipoloPing($address, $rssi, $quickInfo)"
    }
}
