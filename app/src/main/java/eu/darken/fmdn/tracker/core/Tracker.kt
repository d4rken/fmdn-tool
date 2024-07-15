package eu.darken.fmdn.tracker.core

import okio.ByteString
import java.time.Instant

interface Tracker {
    val id: Id

    val address: String

    val rssi: Int

    val raw: ByteString?

    val lastSeen: Instant

    data class Id(
        val value: String
    )
}