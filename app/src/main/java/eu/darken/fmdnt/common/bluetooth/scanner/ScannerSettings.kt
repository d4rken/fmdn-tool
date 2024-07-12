package eu.darken.fmdnt.common.bluetooth.scanner

import android.bluetooth.le.ScanFilter

data class ScannerSettings(
    val filters: Set<ScanFilter> = emptySet(),
    val scannerMode: ScannerMode = ScannerMode.BALANCED,
    val disableOffloadFiltering: Boolean = false,
    val disableOffloadBatching: Boolean = false,
    val disableDirectScanCallback: Boolean = false,
)