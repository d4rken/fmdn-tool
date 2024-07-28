package eu.darken.fmdn.tracker.core.chipolo

import eu.darken.fmdn.sonar.core.devices.chipolo.ChipoloTrackerPing
import eu.darken.fmdn.sonar.core.devices.tile.TileTrackerPing
import eu.darken.fmdn.tracker.core.Tracker

interface ChipoloTracker : Tracker {

    override val lastPing: ChipoloTrackerPing?

}