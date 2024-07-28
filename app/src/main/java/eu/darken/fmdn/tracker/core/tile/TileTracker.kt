package eu.darken.fmdn.tracker.core.tile

import eu.darken.fmdn.sonar.core.devices.tile.TileTrackerPing
import eu.darken.fmdn.tracker.core.Tracker

interface TileTracker : Tracker {

    override val lastPing: TileTrackerPing?

}