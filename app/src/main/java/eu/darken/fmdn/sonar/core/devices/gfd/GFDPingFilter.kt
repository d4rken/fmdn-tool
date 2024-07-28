package eu.darken.fmdn.sonar.core.devices.gfd

import android.bluetooth.le.ScanFilter
import android.os.ParcelUuid
import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import eu.darken.fmdn.common.asHex
import eu.darken.fmdn.common.coroutine.AppScope
import eu.darken.fmdn.common.debug.logging.Logging
import eu.darken.fmdn.common.debug.logging.Logging.Priority.ERROR
import eu.darken.fmdn.common.debug.logging.Logging.Priority.VERBOSE
import eu.darken.fmdn.common.debug.logging.Logging.Priority.WARN
import eu.darken.fmdn.common.debug.logging.asLog
import eu.darken.fmdn.common.debug.logging.log
import eu.darken.fmdn.common.debug.logging.logTag
import eu.darken.fmdn.common.flow.replayingShare
import eu.darken.fmdn.sonar.core.ble.BleScanResult
import eu.darken.fmdn.sonar.core.devices.PingFilter
import eu.darken.fmdn.sonar.core.devices.afn.AFNPingFilter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@Reusable class GFDPingFilter @Inject constructor(
    @AppScope private val appScope: CoroutineScope,
) : PingFilter {

    override val scanFilters = flowOf<Set<ScanFilter>>(
        setOf(
            ScanFilter.Builder().apply {
                // https://developers.google.com/nearby/fast-pair/specifications/extensions/fmdn?hl=de#advertised-frames
                val size = 1
                setServiceData(
                    UID_SERVICE,
                    ByteArray(size).apply {},
                    ByteArray(size).apply {}
                )
            }.build()
        )
    ).replayingShare(appScope)

    override suspend fun parse(scan: BleScanResult): GFDTrackerPing? {
        if (!scanFilters.first().any { it.matches(scan.scanResult) }) {
            return null
        }

        val ping = try {
            val raw = scan.scanResult.scanRecord!!.serviceData[UID_SERVICE]!!.asUByteArray()
            DefaultGFDPing(
                scanResult = scan,
                raw = raw
            )
        } catch (e: Exception) {
            log(TAG, ERROR) { "Failed to parse $scan: ${e.asLog()}" }
            null
        }

        if (ping == null) {
            log(TAG, WARN) { "Could not parse $scan" }
            return null
        }

        log(TAG, VERBOSE) { "Parsed $ping from from [${ping.raw.asHex()}]" }
        log(TAG) { "Parsed $ping" }

        return ping
    }

    @Module @InstallIn(SingletonComponent::class)
    abstract class DIM {
        @Binds @IntoSet abstract fun mod(mod: GFDPingFilter): PingFilter
    }

    companion object {
        val UID_SERVICE: ParcelUuid = ParcelUuid.fromString("0000feaa-0000-1000-8000-00805f9b34fb")
        private val UID_MASK: ParcelUuid = ParcelUuid.fromString("0000ffff-0000-0000-0000-000000000000")

        private val TAG = logTag("Tracker", "Sonar", "PingFilter", "GFD")
    }
}