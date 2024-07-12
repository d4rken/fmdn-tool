package eu.darken.fmdnt.common.debug.autoreport

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import eu.darken.fmdnt.common.InstallId
import eu.darken.fmdnt.common.debug.Bugs
import eu.darken.fmdnt.common.debug.logging.log
import eu.darken.fmdnt.common.debug.logging.logTag
import eu.darken.fmdnt.main.core.GeneralSettings
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AutoReporting @Inject constructor(
    @ApplicationContext private val context: Context,
    private val generalSettings: GeneralSettings,
    private val installId: InstallId,
) {

    fun setup() {
        val isEnabled = generalSettings.isAutoReportingEnabled.flow
        log(TAG) { "setup(): isEnabled=$isEnabled" }

        // NOOP
        Bugs.ready = true
    }

    companion object {
        private val TAG = logTag("Debug", "AutoReporting")
    }
}