package eu.darken.fmdn.tracker.core.samsung

import eu.darken.fmdn.common.ca.CaString
import eu.darken.fmdn.sonar.core.TrackerPing
import eu.darken.fmdn.sonar.core.devices.samsung.SamsungTrackerPing
import eu.darken.fmdn.tracker.core.Tracker
import eu.darken.fmdn.tracker.core.tile.TileTracker

data class DefaultSamsungTracker(
    override val id: Tracker.Id,
    override val label: CaString,
    override val lastPing: SamsungTrackerPing?
) : SamsungTracker