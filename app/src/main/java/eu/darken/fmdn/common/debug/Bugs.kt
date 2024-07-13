package eu.darken.fmdn.common.debug

import eu.darken.fmdn.common.debug.logging.Logging.Priority.VERBOSE
import eu.darken.fmdn.common.debug.logging.Logging.Priority.WARN
import eu.darken.fmdn.common.debug.logging.log
import eu.darken.fmdn.common.debug.logging.logTag

object Bugs {
    var ready = false
    fun report(exception: Exception) {
        log(TAG, VERBOSE) { "Reporting $exception" }
        if (!ready) {
            log(TAG, WARN) { "Bug tracking not initialized yet." }
            return
        }
        // NOOP
    }

    private val TAG = logTag("Debug", "Bugs")
}