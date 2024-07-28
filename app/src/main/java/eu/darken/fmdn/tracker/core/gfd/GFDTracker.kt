package eu.darken.fmdn.tracker.core.gfd

import eu.darken.fmdn.sonar.core.TrackerPing
import eu.darken.fmdn.sonar.core.devices.gfd.GFDTrackerPing
import eu.darken.fmdn.tracker.core.Tracker

interface GFDTracker : Tracker {

    override val lastPing: GFDTrackerPing?

}