package eu.darken.fmdnt.tracker.core.findmy

import android.bluetooth.le.ScanFilter
import dagger.Reusable
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@Reusable
class FindMyParser @Inject constructor() {

    val scanFilters = flowOf<Set<ScanFilter>>(setOf())
}