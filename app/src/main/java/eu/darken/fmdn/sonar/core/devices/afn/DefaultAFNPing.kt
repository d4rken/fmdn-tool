package eu.darken.fmdn.sonar.core.devices.afn

import eu.darken.fmdn.common.collections.isBitSet
import eu.darken.fmdn.sonar.core.ble.BleScanResult
import eu.darken.fmdn.sonar.core.devices.afn.AFNTrackerPing.BatteryLevel
import eu.darken.fmdn.sonar.core.devices.afn.AFNTrackerPing.DeviceType
import eu.darken.fmdn.sonar.core.devices.afn.AFNTrackerPing.State

data class DefaultAFNPing(
    override val scanResult: BleScanResult,
    override val raw: UByteArray,
) : AFNTrackerPing {

    private val rawStatus: UByte?
        get() = raw.getOrNull(2)

    private val rawDeviceType: Int?
        get() = rawStatus?.let { (it.toInt() and 0x30) shr 4 }

    private val rawBattery: Int?
        get() = rawStatus?.toInt()?.let { (it shr 6) and 0b11 }

    override val deviceType: DeviceType
        get() = when (rawDeviceType) {
            0 -> DeviceType.APPLE
            1 -> DeviceType.AIRTAG
            2 -> DeviceType.THIRDPARTY
            3 -> DeviceType.AIRPOD
            else -> DeviceType.UNKNOWN
        }

    override val batteryLevel: BatteryLevel
        get() = when (rawBattery) {
            0 -> BatteryLevel.FULL
            1 -> BatteryLevel.MEDIUM
            2 -> BatteryLevel.LOW
            3 -> BatteryLevel.CRITICALLY_LOW
            else -> BatteryLevel.UNKNOWN
        }

    override val state: State
        get() = when {
            rawStatus?.isBitSet(2) == true -> State.MAINTAINED
            rawStatus?.isBitSet(2) == false -> State.NOT_MAINTAINED
            else -> State.UNKNOWN
        }

    override val quickInfo: String
        get() = "$deviceType, $batteryLevel, $state"

    override fun toString(): String {
        return "DefaultAFNPing($address, $rssi, $quickInfo, $lastSeen)"
    }
}
