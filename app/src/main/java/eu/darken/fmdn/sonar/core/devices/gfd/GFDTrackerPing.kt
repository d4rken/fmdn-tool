package eu.darken.fmdn.sonar.core.devices.gfd

import eu.darken.fmdn.sonar.core.TrackerPing

interface GFDTrackerPing : TrackerPing {

    enum class TrackingProtectionMode {
        TRACKING_PROTECTION,
        NORMAL,
        UNKNOWN
    }

    val trackingProtectionMode: TrackingProtectionMode
        get() = when (raw.getOrNull(0)) {
            0x40.toUByte() -> TrackingProtectionMode.NORMAL
            0x41.toUByte() -> TrackingProtectionMode.TRACKING_PROTECTION
            else -> TrackingProtectionMode.UNKNOWN
        }

}