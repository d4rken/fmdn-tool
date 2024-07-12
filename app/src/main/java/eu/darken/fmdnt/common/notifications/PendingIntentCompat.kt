package eu.darken.fmdnt.common.notifications

import android.app.PendingIntent
import eu.darken.fmdnt.common.hasApiLevel

object PendingIntentCompat {
    val FLAG_IMMUTABLE: Int = if (hasApiLevel(31)) {
        PendingIntent.FLAG_IMMUTABLE
    } else {
        0
    }
    val FLAG_MUTABLE: Int = if (hasApiLevel(31)) {
        PendingIntent.FLAG_MUTABLE
    } else {
        0
    }
}