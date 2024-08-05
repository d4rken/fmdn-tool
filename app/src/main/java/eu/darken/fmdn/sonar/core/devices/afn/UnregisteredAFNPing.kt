package eu.darken.fmdn.sonar.core.devices.afn

import eu.darken.fmdn.sonar.core.ble.BleScanResult
import eu.darken.fmdn.sonar.core.devices.afn.AFNTrackerPing.State

data class UnregisteredAFNPing(
    override val scanResult: BleScanResult,
    override val raw: UByteArray,
) : AFNTrackerPing {

    override val state: State
        get() = State.UNREGISTERED

    override val quickInfo: String
        get() = "$deviceType, $batteryLevel, $state"

    override fun toString(): String {
        return "DefaultAFNPing($address, $rssi, $quickInfo, $lastSeen)"
    }
}
