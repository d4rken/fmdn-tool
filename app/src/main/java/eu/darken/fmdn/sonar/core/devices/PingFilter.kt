package eu.darken.fmdn.sonar.core.devices

import android.bluetooth.le.ScanFilter
import eu.darken.fmdn.sonar.core.TrackerPing
import eu.darken.fmdn.sonar.core.ble.BleScanResult
import kotlinx.coroutines.flow.Flow

interface PingFilter {
    val scanFilters: Flow<Set<ScanFilter>>

    suspend fun parse(scan: BleScanResult): TrackerPing?
}