package eu.darken.fmdn.tracker.core.afn

import android.bluetooth.le.ScanFilter
import dagger.Reusable
import eu.darken.fmdn.common.debug.logging.logTag
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@Reusable
class AFNParser @Inject constructor() {

    val scanFilters = flowOf<Set<ScanFilter>>(
        setOf(
            ScanFilter.Builder().apply {
                val CONTINUITY_PROTOCOL_MESSAGE_LENGTH = 27
                val CONTINUITY_PROTOCOL_MESSAGE_TYPE_PROXIMITY_PAIRING = 0x07.toUByte()
                val PAIRING_MESSAGE_LENGTH = 25
                val manufacturerData = ByteArray(CONTINUITY_PROTOCOL_MESSAGE_LENGTH).apply {
                    this[0] = CONTINUITY_PROTOCOL_MESSAGE_TYPE_PROXIMITY_PAIRING.toByte()
                    this[1] = PAIRING_MESSAGE_LENGTH.toByte()
                }

                val manufacturerDataMask = ByteArray(CONTINUITY_PROTOCOL_MESSAGE_LENGTH).apply {
                    this[0] = 1
                    this[1] = 1
                }
                val applecompanyIdentifier = 0x004C
                setManufacturerData(
                    applecompanyIdentifier,
                    manufacturerData,
                    manufacturerDataMask
                )
            }.build()
        )
    )

    companion object {
        private val TAG = logTag("Tracker", "Parser", "AFN")
    }
}