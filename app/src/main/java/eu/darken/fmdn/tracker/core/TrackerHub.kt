package eu.darken.fmdn.tracker.core

import eu.darken.fmdn.common.bluetooth.scanner.BleScanner
import eu.darken.fmdn.common.bluetooth.scanner.ScannerSettings
import eu.darken.fmdn.common.coroutine.AppScope
import eu.darken.fmdn.common.debug.logging.Logging.Priority.VERBOSE
import eu.darken.fmdn.common.debug.logging.Logging.Priority.WARN
import eu.darken.fmdn.common.debug.logging.asLog
import eu.darken.fmdn.common.debug.logging.log
import eu.darken.fmdn.common.debug.logging.logTag
import eu.darken.fmdn.common.flow.combine
import eu.darken.fmdn.common.flow.replayingShare
import eu.darken.fmdn.main.core.GeneralSettings
import eu.darken.fmdn.tracker.core.afn.AFNParser
import eu.darken.fmdn.tracker.core.gfd.GFDParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.time.Duration
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackerHub @Inject constructor(
    @AppScope private val appScope: CoroutineScope,
    private val bleScanner: BleScanner,
    private val gfdParser: GFDParser,
    private val afnParser: AFNParser,
    private val settings: GeneralSettings,
) {

    private val scannerSettings = combine(
        settings.scannerMode.flow,
        settings.isOffloadedFilteringDisabled.flow,
        settings.isOffloadedBatchingDisabled.flow,
        settings.useIndirectScanResultCallback.flow,
        gfdParser.scanFilters,
        afnParser.scanFilters,
    ) { mode, filteringDisabled, batchingDisabled, useIndirectCallback, gfdFilters, afnFilters ->
        ScannerSettings(
            filters = gfdFilters + afnFilters,
            scannerMode = mode,
            disableOffloadFiltering = filteringDisabled,
            disableOffloadBatching = batchingDisabled,
            disableDirectScanCallback = useIndirectCallback
        )
    }

    private val cache = ConcurrentHashMap<String, Tracker>()

    val devices: Flow<Set<Tracker>> = scannerSettings
        .flatMapLatest { settings ->
            try {
                bleScanner.scan(settings)
            } catch (e: Exception) {
                log(TAG) { "Scanner failed: ${e.asLog()}" }
                emptyFlow()
            }
        }
        .map { results ->
            log(TAG) { "Processing ${results.size} scan results" }
            results.mapNotNull { result ->
                var tracker: Tracker? = null

                if (tracker == null) {
                    tracker = gfdParser.parse(result).also { log(TAG, VERBOSE) { "Parsed $it <- $result" } }
                }

                if (tracker == null) {
                    tracker = afnParser.parse(result).also { log(TAG, VERBOSE) { "Parsed $it <- $result" } }
                }

                if (tracker == null) log(TAG, WARN) { "No parser match: $result" }

                tracker
            }.toSet()
        }
        .map { trackers ->
            cache.putAll(trackers.associateBy { it.address })
            val now = Instant.now()
            cache.values.forEach {
                if (Duration.between(it.lastSeen, now) > Duration.ofSeconds(60)) {
                    cache.remove(it.address)
                }
            }
            cache.values.toSet()
        }
        .replayingShare(appScope)

    companion object {
        private val TAG = logTag("Tracker", "Hub")
    }
}