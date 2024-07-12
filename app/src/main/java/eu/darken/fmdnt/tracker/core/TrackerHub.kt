package eu.darken.fmdnt.tracker.core

import eu.darken.fmdnt.common.bluetooth.scanner.BleScanner
import eu.darken.fmdnt.common.bluetooth.scanner.ScannerSettings
import eu.darken.fmdnt.tracker.core.fmdn.FMDNParser
import eu.darken.fmdnt.main.core.GeneralSettings
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackerHub @Inject constructor(
    private val bleScanner: BleScanner,
    private val fmdnParser: FMDNParser,
    private val settings: GeneralSettings,
) {

    private val scannerSettings = combine(
        settings.scannerMode.flow,
        settings.isOffloadedFilteringDisabled.flow,
        settings.isOffloadedBatchingDisabled.flow,
        settings.useIndirectScanResultCallback.flow,
        fmdnParser.scanFilters,
    ) { mode, filteringDisabled, batchingDisabled, useIndirectCallback, fmdnFilters ->
        ScannerSettings(
            filters = fmdnFilters,
            scannerMode = mode,
            disableOffloadFiltering = filteringDisabled,
            disableOffloadBatching = batchingDisabled,
            disableDirectScanCallback = useIndirectCallback
        )
    }
}