package eu.darken.fmdn.tracker.core.gfd

import eu.darken.fmdn.common.ca.CaString
import eu.darken.fmdn.sonar.core.TrackerPing
import eu.darken.fmdn.sonar.core.devices.gfd.GFDTrackerPing
import eu.darken.fmdn.tracker.core.Tracker

data class DefaultGFDTracker(
    override val id: Tracker.Id,
    override val label: CaString,
    override val lastPing: GFDTrackerPing?
) : GFDTracker