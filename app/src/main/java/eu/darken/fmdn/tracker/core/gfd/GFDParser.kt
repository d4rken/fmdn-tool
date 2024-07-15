package eu.darken.fmdn.tracker.core.gfd

import android.bluetooth.le.ScanFilter
import android.os.ParcelUuid
import dagger.Reusable
import eu.darken.fmdn.common.bluetooth.scanner.BleScanResult
import eu.darken.fmdn.common.coroutine.AppScope
import eu.darken.fmdn.common.debug.logging.log
import eu.darken.fmdn.common.debug.logging.logTag
import eu.darken.fmdn.common.flow.replayingShare
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@Reusable class GFDParser @Inject constructor(
    @AppScope private val appScope: CoroutineScope,
) {

    val scanFilters = flowOf<Set<ScanFilter>>(
        setOf(
            ScanFilter.Builder().apply {
                // https://developers.google.com/nearby/fast-pair/specifications/extensions/fmdn?hl=de#advertised-frames
                val MESSAGE_LENGTH = 1
                setServiceData(
                    UID_SERVICE,
                    ByteArray(MESSAGE_LENGTH).apply {},
                    ByteArray(MESSAGE_LENGTH).apply {}
                )
            }.build()
        )
    )
        .replayingShare(appScope)

    suspend fun parse(scan: BleScanResult): GFDTracker? {
        if (scanFilters.first().any { it.matches(scan.scanResult) }) {
            log(TAG) { "parse(): $scan" }
        } else {
            return null
        }

        return DefaultGFDTracker(
            scanResult = scan
        )
    }

    companion object {
        private val UID_SERVICE: ParcelUuid = ParcelUuid.fromString("0000feaa-0000-1000-8000-00805f9b34fb")
        private val UID_MASK: ParcelUuid = ParcelUuid.fromString("0000ffff-0000-0000-0000-000000000000")

        private val TAG = logTag("Tracker", "Parser", "GFD")
    }
}