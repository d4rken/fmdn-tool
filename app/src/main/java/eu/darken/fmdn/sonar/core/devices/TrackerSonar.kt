package eu.darken.fmdn.sonar.core.devices

import android.Manifest
import android.annotation.SuppressLint
import androidx.annotation.RequiresPermission
import eu.darken.fmdn.common.coroutine.AppScope
import eu.darken.fmdn.common.debug.logging.Logging.Priority.INFO
import eu.darken.fmdn.common.debug.logging.Logging.Priority.WARN
import eu.darken.fmdn.common.debug.logging.asLog
import eu.darken.fmdn.common.debug.logging.log
import eu.darken.fmdn.common.debug.logging.logTag
import eu.darken.fmdn.common.flow.combine
import eu.darken.fmdn.common.flow.replayingShare
import eu.darken.fmdn.sonar.core.SonarSettings
import eu.darken.fmdn.sonar.core.TrackerPing
import eu.darken.fmdn.sonar.core.ble.BleScanner
import eu.darken.fmdn.sonar.core.ble.ScannerSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackerSonar @Inject constructor(
    @AppScope private val appScope: CoroutineScope,
    settings: SonarSettings,
    private val bleScanner: BleScanner,
    private val pingFilters: Set<@JvmSuppressWildcards PingFilter>,
) {
    init {
        log(TAG, INFO) { "${pingFilters.size} ping filters loaded:" }
        pingFilters.forEachIndexed { index, pingFilter -> log(TAG, INFO) { "Loaded #$index: $pingFilter" } }
    }

    private val scannerSettings = combine(
        settings.scannerMode.flow,
        settings.isOffloadedFilteringDisabled.flow,
        settings.isOffloadedBatchingDisabled.flow,
        settings.useIndirectScanResultCallback.flow,
        kotlinx.coroutines.flow.combine(pingFilters.map { it.scanFilters }) { sets -> sets.flatMap { it }.toSet() },
    ) { mode, filteringDisabled, batchingDisabled, useIndirectCallback, pingFilters ->
        ScannerSettings(
            filters = pingFilters,
            scannerMode = mode,
            disableOffloadFiltering = filteringDisabled,
            disableOffloadBatching = batchingDisabled,
            disableDirectScanCallback = useIndirectCallback
        )
    }

    @SuppressLint("MissingPermission")
    @RequiresPermission(anyOf = [Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH])
    val pings: Flow<Set<TrackerPing>> = scannerSettings
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
                val tracker: TrackerPing? = pingFilters.firstNotNullOfOrNull { it.parse(result) }

                if (tracker == null) log(TAG, WARN) { "No parser match: $result" }

                tracker
            }.toSet()
        }
        .replayingShare(appScope)

    companion object {
        private val TAG = logTag("Tracker", "Sonar")
    }
}