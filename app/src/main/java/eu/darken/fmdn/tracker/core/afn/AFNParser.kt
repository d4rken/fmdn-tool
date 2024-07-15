package eu.darken.fmdn.tracker.core.afn

import android.bluetooth.le.ScanFilter
import dagger.Reusable
import eu.darken.fmdn.common.bluetooth.scanner.BleScanResult
import eu.darken.fmdn.common.debug.logging.log
import eu.darken.fmdn.common.debug.logging.logTag
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@Reusable
class AFNParser @Inject constructor() {

    val scanFilters = flowOf<Set<ScanFilter>>(
        setOf(
            ScanFilter.Builder().apply {
                // General continuity protocol style
                // https://adamcatley.com/AirTag.html
                val manufacturerData = ByteArray(2).apply {
                    this[0] = 0x12 // Apple payload type to indicate a FindMy network broadcast
                    this[1] = 0x19 // Apple payload length (31 - 6 = 25 = 0x19)
                }

                val manufacturerDataMask = ByteArray(manufacturerData.size).apply {
                    this[0] = 1
                    this[1] = 1
                }
                setManufacturerData(
                    APPLE_IDENTIFIER,
                    manufacturerData,
                    manufacturerDataMask
                )
            }.build()
        )
    )

    suspend fun parse(scan: BleScanResult): AFNTracker? {
        if (scanFilters.first().any { it.matches(scan.scanResult) }) {
            log(TAG) { "parse(): $scan" }
        } else {
            return null
        }

        return DefaultAFNTracker(
            scanResult = scan
        )
    }

    companion object {
        private const val APPLE_IDENTIFIER = 0x004C
        private val TAG = logTag("Tracker", "Parser", "AFN")
    }
}