package eu.darken.fmdn.tracker.core.afn

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import eu.darken.fmdn.common.ca.caString
import eu.darken.fmdn.common.ca.toCaString
import eu.darken.fmdn.common.debug.logging.Logging
import eu.darken.fmdn.common.debug.logging.Logging.Priority.WARN
import eu.darken.fmdn.common.debug.logging.log
import eu.darken.fmdn.common.debug.logging.logTag
import eu.darken.fmdn.sonar.core.devices.TrackerSonar
import eu.darken.fmdn.sonar.core.devices.afn.AFNTrackerPing
import eu.darken.fmdn.tracker.core.HubModule
import eu.darken.fmdn.tracker.core.Tracker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AFNModule @Inject constructor(
    private val sonar: TrackerSonar,
) : HubModule {

    override val trackers: Flow<Set<Tracker>> = combine(
        sonar.pings.map { pings -> pings.filterIsInstance<AFNTrackerPing>() },
        flowOf(Unit),
    ) { pings, _ ->
        pings.map { ping ->
            DefaultAFNTracker(
                id = Tracker.Id(ping.address),
                label = try {
                    ping.device.name?.toCaString() ?:  caString { "Apple Tracker" }
                } catch (e: Exception) {
                    log(TAG, WARN) { "Failed to get device name: $e" }
                    caString { "Apple Tracker" }
                },
                lastPing = ping,
            )
        }.toSet()
    }

    @Module @InstallIn(SingletonComponent::class)
    abstract class DIM {
        @Binds @IntoSet abstract fun mod(mod: AFNModule): HubModule
    }

    companion object {
        private val TAG = logTag("Tracker", "Hub", "Module", "AFN")
    }
}