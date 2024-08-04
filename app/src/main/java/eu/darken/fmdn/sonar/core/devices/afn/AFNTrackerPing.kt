package eu.darken.fmdn.sonar.core.devices.afn

import eu.darken.fmdn.sonar.core.TrackerPing

interface AFNTrackerPing : TrackerPing {

    enum class DeviceType {
        APPLE,
        THIRDPARTY,
        AIRTAG,
        AIRPOD,
        UNKNOWN,
    }

    val deviceType: DeviceType
        get() = DeviceType.UNKNOWN

    enum class BatteryLevel {
        FULL,
        MEDIUM,
        LOW,
        CRITICALLY_LOW,
        UNKNOWN,
    }

    val batteryLevel: BatteryLevel
        get() = BatteryLevel.UNKNOWN

    enum class State {
        MAINTAINED,
        NOT_MAINTAINED,
        LOST,
        UNREGISTERED,
        UNKNOWN,
    }

    val state: State
        get() = State.UNKNOWN

}