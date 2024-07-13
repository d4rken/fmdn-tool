package eu.darken.fmdn.main.ui

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.darken.fmdn.common.coroutine.DispatcherProvider
import eu.darken.fmdn.common.debug.logging.Logging.Priority.VERBOSE
import eu.darken.fmdn.common.debug.logging.log
import eu.darken.fmdn.common.uix.ViewModel2
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class MainActivityVM @Inject constructor(
    dispatcherProvider: DispatcherProvider,
    handle: SavedStateHandle,
) : ViewModel2(dispatcherProvider = dispatcherProvider) {

    private val stateFlow = MutableStateFlow(State())
    val state = stateFlow
        .onEach { log(VERBOSE) { "New state: $it" } }
        .asLiveData2()

    private val readyStateInternal = MutableStateFlow(true)
    val readyState = readyStateInternal.asLiveData2()

    init {
        log { "ViewModel: $ this" }
        log { "SavedStateHandle: ${handle.keys()}" }
        log { "Persisted value: ${handle.get<String>("key")}" }
        handle.set("key", "valueActivity")
    }

    fun onGo() {
        stateFlow.value = stateFlow.value.copy(ready = true)
    }

    data class State(
        val ready: Boolean = false
    )

}