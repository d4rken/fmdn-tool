package eu.darken.fmdn.tracker.core.gfd

import android.bluetooth.le.ScanFilter
import dagger.Reusable
import eu.darken.fmdn.common.debug.logging.logTag
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@Reusable
class GFDParser @Inject constructor() {

    val scanFilters = flowOf<Set<ScanFilter>>(
        setOf(
            ScanFilter.Builder().apply {

            }.build()
        )
    )

    companion object {
        private val TAG = logTag("Tracker", "Parser", "GFD")
    }
}