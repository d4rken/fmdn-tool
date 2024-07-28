package eu.darken.fmdn.tracker.core.chipolo

import eu.darken.fmdn.common.ca.CaString
import eu.darken.fmdn.sonar.core.TrackerPing
import eu.darken.fmdn.sonar.core.devices.chipolo.ChipoloTrackerPing
import eu.darken.fmdn.sonar.core.devices.tile.TileTrackerPing
import eu.darken.fmdn.tracker.core.Tracker
import eu.darken.fmdn.tracker.core.tile.TileTracker

data class DefaultChipoloTracker(
    override val id: Tracker.Id,
    override val label: CaString,
    override val lastPing: ChipoloTrackerPing?
) : ChipoloTracker