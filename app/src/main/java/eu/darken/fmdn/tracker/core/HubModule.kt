package eu.darken.fmdn.tracker.core

import kotlinx.coroutines.flow.Flow

interface HubModule {
    val trackers: Flow<Set<Tracker>>
}