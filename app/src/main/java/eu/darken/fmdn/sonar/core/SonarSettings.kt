package eu.darken.fmdn.sonar.core

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.squareup.moshi.Moshi
import dagger.hilt.android.qualifiers.ApplicationContext
import eu.darken.fmdn.common.datastore.PreferenceScreenData
import eu.darken.fmdn.common.datastore.PreferenceStoreMapper
import eu.darken.fmdn.common.datastore.createValue
import eu.darken.fmdn.common.debug.logging.logTag
import eu.darken.fmdn.sonar.core.ble.ScannerMode
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SonarSettings @Inject constructor(
    @ApplicationContext private val context: Context,
    moshi: Moshi,
) : PreferenceScreenData {

    private val Context.dataStore by preferencesDataStore(name = "settings_sonar")

    override val dataStore: DataStore<Preferences>
        get() = context.dataStore

    val scannerMode = dataStore.createValue("core.scanner.mode", ScannerMode.LOW_LATENCY, moshi)
    val isOffloadedFilteringDisabled = dataStore.createValue("core.compat.offloaded.filtering.disabled", false)
    val isOffloadedBatchingDisabled = dataStore.createValue("core.compat.offloaded.batching.disabled", false)
    val useIndirectScanResultCallback = dataStore.createValue("core.compat.indirectcallback.enabled", false)

    override val mapper = PreferenceStoreMapper(
        // TODO ?
    )

    companion object {
        internal val TAG = logTag("Tracker", "Sonar", "Settings")
    }
}