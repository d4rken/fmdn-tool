package eu.darken.fmdn.sonar.core.devices.afn

import eu.darken.fmdn.sonar.core.ble.BleScanResult

data class DefaultAFNPing(
    override val scanResult: BleScanResult,
    override val raw: UByteArray,
) : AFNTrackerPing {

    override val quickInfo: String
        get() = "$deviceType, $batteryLevel, $state"

    override fun toString(): String {
        return "DefaultAFNPing($address, $rssi, $quickInfo)"
    }
}
