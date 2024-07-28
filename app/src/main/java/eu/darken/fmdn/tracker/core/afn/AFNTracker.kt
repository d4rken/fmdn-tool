package eu.darken.fmdn.tracker.core.afn

import eu.darken.fmdn.sonar.core.devices.afn.AFNTrackerPing
import eu.darken.fmdn.tracker.core.Tracker

interface AFNTracker : Tracker {

    override val lastPing: AFNTrackerPing?

}