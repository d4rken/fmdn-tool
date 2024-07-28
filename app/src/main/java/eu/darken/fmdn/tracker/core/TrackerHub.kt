package eu.darken.fmdn.tracker.core

import eu.darken.fmdn.common.coroutine.AppScope
import eu.darken.fmdn.common.debug.logging.logTag
import eu.darken.fmdn.common.flow.replayingShare
import eu.darken.fmdn.common.flow.setupCommonEventHandlers
import eu.darken.fmdn.main.core.GeneralSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.time.Duration
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackerHub @Inject constructor(
    @AppScope private val appScope: CoroutineScope,
    private val settings: GeneralSettings,
    private val hubModules: Set<@JvmSuppressWildcards HubModule>,
) {

    private val cache = ConcurrentHashMap<Tracker.Id, Tracker>()

    val trackers: Flow<Set<Tracker>> = combine(
        flowOf(Unit),
        combine(hubModules.map { it.trackers }) { sets -> sets.flatMap { it }.toSet() }
    ) { _, trackers ->
        trackers
    }
        .map { trackers ->
            cache.putAll(trackers.associateBy { it.id })
            val now = Instant.now()
            cache.values.forEach {
                if (Duration.between(it.lastSeen, now) > Duration.ofSeconds(60)) {
                    cache.remove(it.id)
                }
            }
            cache.values.toSet()
        }
        .setupCommonEventHandlers(TAG) { "trackers" }
        .replayingShare(appScope)

    companion object {
        private val TAG = logTag("Tracker", "Hub")
    }
}