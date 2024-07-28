package eu.darken.fmdn.tracker.core

import eu.darken.fmdn.common.ca.CaString
import eu.darken.fmdn.sonar.core.TrackerPing
import java.time.Instant

interface Tracker {
    val id: Id

    val label: CaString

    val lastSeen: Instant?
        get() = lastPing?.lastSeen

    val lastPing: TrackerPing?

    data class Id(
        val value: String
    )
}