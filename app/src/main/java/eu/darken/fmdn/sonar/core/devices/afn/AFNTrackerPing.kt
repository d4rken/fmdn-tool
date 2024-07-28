package eu.darken.fmdn.sonar.core.devices.afn

import eu.darken.fmdn.common.collections.isBitSet
import eu.darken.fmdn.sonar.core.TrackerPing

interface AFNTrackerPing : TrackerPing {

    val rawStatus: UByte?
        get() = raw.getOrNull(2)

    enum class DeviceType {
        APPLE,
        THIRDPARTY_TRACKER,
        AIRTAG,
        AIRPOD,
        UNKNOWN,
        ;
    }

    val rawDeviceType: Int?
        get() = rawStatus?.let { (it.toInt() and 0x30) shr 4 }

    val deviceType: DeviceType
        get() = when (rawDeviceType) {
            0 -> DeviceType.APPLE
            1 -> DeviceType.AIRTAG
            2 -> DeviceType.THIRDPARTY_TRACKER
            3 -> DeviceType.AIRPOD
            else -> DeviceType.UNKNOWN
        }

    enum class BatteryLevel {
        FULL,
        MEDIUM,
        LOW,
        CRITICALLY_LOW,
        UNKNOWN,
    }

    val rawBattery: Int?
        get() = rawStatus?.toInt()?.let { (it shr 6) and 0b11 }

    val batteryLevel: BatteryLevel
        get() = when (rawBattery) {
            0 -> BatteryLevel.FULL
            1 -> BatteryLevel.MEDIUM
            2 -> BatteryLevel.LOW
            3 -> BatteryLevel.CRITICALLY_LOW
            else -> BatteryLevel.UNKNOWN
        }

    enum class State {
        MAINTAINED,
        NOT_MAINTAINED,
        UNKNOWN
    }

    val state: State
        get() = when {
            rawStatus?.isBitSet(2) == true -> State.MAINTAINED
            rawStatus?.isBitSet(2) == false -> State.NOT_MAINTAINED
            else -> State.UNKNOWN
        }
}