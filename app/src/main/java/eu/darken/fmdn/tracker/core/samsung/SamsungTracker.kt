package eu.darken.fmdn.tracker.core.samsung

import eu.darken.fmdn.sonar.core.TrackerPing
import eu.darken.fmdn.sonar.core.devices.samsung.SamsungTrackerPing
import eu.darken.fmdn.tracker.core.Tracker

interface SamsungTracker : Tracker {

    override val lastPing: SamsungTrackerPing?

}