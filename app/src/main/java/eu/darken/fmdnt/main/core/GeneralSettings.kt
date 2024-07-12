package eu.darken.fmdnt.main.core

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.squareup.moshi.Moshi
import dagger.hilt.android.qualifiers.ApplicationContext
import eu.darken.fmdnt.common.bluetooth.scanner.ScannerMode
import eu.darken.fmdnt.common.datastore.PreferenceScreenData
import eu.darken.fmdnt.common.datastore.PreferenceStoreMapper
import eu.darken.fmdnt.common.datastore.createValue
import eu.darken.fmdnt.common.debug.logging.logTag
import eu.darken.fmdnt.common.theming.ThemeMode
import eu.darken.fmdnt.common.theming.ThemeStyle
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeneralSettings @Inject constructor(
    @ApplicationContext private val context: Context,
    moshi: Moshi,
) : PreferenceScreenData {

    private val Context.dataStore by preferencesDataStore(name = "settings_core")

    override val dataStore: DataStore<Preferences>
        get() = context.dataStore

    val isAutoReportingEnabled = dataStore.createValue("debug.bugreport.automatic.enabled", false)

    val themeMode = dataStore.createValue("core.ui.theme.mode", ThemeMode.SYSTEM, moshi)
    val themeStyle = dataStore.createValue("core.ui.theme.style", ThemeStyle.DEFAULT, moshi)

    val scannerMode = dataStore.createValue("core.scanner.mode", ScannerMode.BALANCED, moshi)
    val isOffloadedFilteringDisabled = dataStore.createValue("core.compat.offloaded.filtering.disabled", false)
    val isOffloadedBatchingDisabled = dataStore.createValue("core.compat.offloaded.batching.disabled", false)
    val useIndirectScanResultCallback = dataStore.createValue("core.compat.indirectcallback.enabled", false)

    val isOnboardingDone = dataStore.createValue("core.onboarding.done", false)

    override val mapper = PreferenceStoreMapper(
        isAutoReportingEnabled,
        themeMode,
        themeStyle,
    )

    companion object {
        internal val TAG = logTag("Core", "Settings")
    }
}