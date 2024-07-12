package eu.darken.fmdnt.tracker.core.fmdn

import android.bluetooth.le.ScanFilter
import dagger.Reusable
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@Reusable
class FMDNParser @Inject constructor() {

    val scanFilters = flowOf<Set<ScanFilter>>(setOf())
}