package eu.darken.fmdn.tracker.core.tile

import eu.darken.fmdn.common.ca.CaString
import eu.darken.fmdn.sonar.core.TrackerPing
import eu.darken.fmdn.sonar.core.devices.tile.TileTrackerPing
import eu.darken.fmdn.tracker.core.Tracker

data class DefaultTileTracker(
    override val id: Tracker.Id,
    override val label: CaString,
    override val lastPing: TileTrackerPing?
) : TileTracker