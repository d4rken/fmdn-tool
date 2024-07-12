package eu.darken.fmdnt.main.ui.settings.general

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.darken.fmdnt.common.coroutine.DispatcherProvider
import eu.darken.fmdnt.common.debug.logging.logTag
import eu.darken.fmdnt.common.uix.ViewModel3
import javax.inject.Inject

@HiltViewModel
class GeneralSettingsFragmentVM @Inject constructor(
    private val handle: SavedStateHandle,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel3(dispatcherProvider) {


    companion object {
        private val TAG = logTag("Settings", "General", "VM")
    }
}