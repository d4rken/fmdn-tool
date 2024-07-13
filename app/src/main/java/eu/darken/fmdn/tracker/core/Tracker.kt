package eu.darken.fmdn.tracker.core

interface Tracker {
    val id: Id

    data class Id(
        val value: String
    )
}