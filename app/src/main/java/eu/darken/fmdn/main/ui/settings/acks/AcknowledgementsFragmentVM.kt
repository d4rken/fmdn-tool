package eu.darken.fmdn.main.ui.settings.acks

import androidx.lifecycle.SavedStateHandle
import dagger.assisted.AssistedInject
import eu.darken.fmdn.common.coroutine.DispatcherProvider
import eu.darken.fmdn.common.debug.logging.logTag
import eu.darken.fmdn.common.uix.ViewModel3

class AcknowledgementsFragmentVM @AssistedInject constructor(
    private val handle: SavedStateHandle,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel3(dispatcherProvider) {

    companion object {
        private val TAG = logTag("Settings", "Acknowledgements", "VM")
    }
}