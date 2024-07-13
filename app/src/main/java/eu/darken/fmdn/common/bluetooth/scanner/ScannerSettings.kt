package eu.darken.fmdn.common.bluetooth.scanner

import android.bluetooth.le.ScanFilter

data class ScannerSettings(
    val scannerMode: ScannerMode = ScannerMode.BALANCED,
    val disableOffloadFiltering: Boolean = false,
    val disableOffloadBatching: Boolean = false,
    val disableDirectScanCallback: Boolean = false,
    val filters: Set<ScanFilter> = emptySet(),
)