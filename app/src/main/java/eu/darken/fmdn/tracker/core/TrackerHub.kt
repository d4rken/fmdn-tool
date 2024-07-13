package eu.darken.fmdn.tracker.core

import eu.darken.fmdn.common.bluetooth.scanner.BleScanner
import eu.darken.fmdn.common.bluetooth.scanner.ScannerSettings
import eu.darken.fmdn.common.coroutine.AppScope
import eu.darken.fmdn.common.debug.logging.asLog
import eu.darken.fmdn.common.debug.logging.log
import eu.darken.fmdn.common.debug.logging.logTag
import eu.darken.fmdn.common.flow.replayingShare
import eu.darken.fmdn.main.core.GeneralSettings
import eu.darken.fmdn.tracker.core.gfd.GFDParser
import eu.darken.fmdn.tracker.core.gfd.GFDTracker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackerHub @Inject constructor(
    @AppScope private val appScope: CoroutineScope,
    private val bleScanner: BleScanner,
    private val gfdParser: GFDParser,
    private val settings: GeneralSettings,
) {

    private val scannerSettings = combine(
        settings.scannerMode.flow,
        settings.isOffloadedFilteringDisabled.flow,
        settings.isOffloadedBatchingDisabled.flow,
        settings.useIndirectScanResultCallback.flow,
        gfdParser.scanFilters,
    ) { mode, filteringDisabled, batchingDisabled, useIndirectCallback, gfdFilters ->
        ScannerSettings(
            filters = gfdFilters,
            scannerMode = mode,
            disableOffloadFiltering = filteringDisabled,
            disableOffloadBatching = batchingDisabled,
            disableDirectScanCallback = useIndirectCallback
        )
    }

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
            results.map {
                object : GFDTracker {
                    override val id: Tracker.Id
                        get() = Tracker.Id(UUID.randomUUID().toString())

                }
            }.toSet()
        }
        .replayingShare(appScope)

    companion object {
        private val TAG = logTag("Tracker", "Hub")
    }
}