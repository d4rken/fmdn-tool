package eu.darken.fmdnt.common.uix

import androidx.navigation.NavDirections
import eu.darken.fmdnt.common.coroutine.DispatcherProvider
import eu.darken.fmdnt.common.debug.logging.asLog
import eu.darken.fmdnt.common.debug.logging.log
import eu.darken.fmdnt.common.error.ErrorEventSource
import eu.darken.fmdnt.common.flow.setupCommonEventHandlers
import eu.darken.fmdnt.common.livedata.SingleLiveEvent
import eu.darken.fmdnt.common.navigation.NavEventSource
import eu.darken.fmdnt.common.navigation.navVia
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn


abstract class ViewModel3(
    dispatcherProvider: DispatcherProvider,
) : ViewModel2(dispatcherProvider), NavEventSource, ErrorEventSource {

    override val navEvents = SingleLiveEvent<NavDirections?>()
    override val errorEvents = SingleLiveEvent<Throwable>()

    init {
        launchErrorHandler = CoroutineExceptionHandler { _, ex ->
            log(TAG) { "Error during launch: ${ex.asLog()}" }
            errorEvents.postValue(ex)
        }
    }

    override fun <T> Flow<T>.launchInViewModel() = this
        .setupCommonEventHandlers(TAG) { "launchInViewModel()" }
        .launchIn(vmScope)

    fun NavDirections.navigate() {
        navVia(navEvents)
    }
}