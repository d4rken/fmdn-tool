package eu.darken.fmdnt

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import eu.darken.fmdnt.common.BuildConfigWrap
import eu.darken.fmdnt.common.debug.autoreport.AutoReporting
import eu.darken.fmdnt.common.debug.logging.LogCatLogger
import eu.darken.fmdnt.common.debug.logging.Logging
import eu.darken.fmdnt.common.debug.logging.asLog
import eu.darken.fmdnt.common.debug.logging.log
import eu.darken.fmdnt.common.debug.logging.logTag
import eu.darken.fmdnt.common.theming.Theming
import javax.inject.Inject

@HiltAndroidApp
open class App : Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory
    @Inject lateinit var bugReporter: AutoReporting
    @Inject lateinit var theming: Theming

    override fun onCreate() {
        super.onCreate()
        if (BuildConfigWrap.DEBUG) {
            Logging.install(LogCatLogger())
            log(TAG) { "BuildConfig.DEBUG=true" }
        }

        bugReporter.setup()

        theming.setup()

        log(TAG) { "onCreate() done! ${Exception().asLog()}" }
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(
                when {
                    BuildConfigWrap.DEBUG -> android.util.Log.VERBOSE
                    BuildConfigWrap.BUILD_TYPE == BuildConfigWrap.BuildType.DEV -> android.util.Log.DEBUG
                    BuildConfigWrap.BUILD_TYPE == BuildConfigWrap.BuildType.BETA -> android.util.Log.INFO
                    BuildConfigWrap.BUILD_TYPE == BuildConfigWrap.BuildType.RELEASE -> android.util.Log.WARN
                    else -> android.util.Log.VERBOSE
                }
            )
            .setWorkerFactory(workerFactory)
            .build()

    companion object {
        internal val TAG = logTag("App")
    }
}
