package eu.darken.fmdn.common.error

import eu.darken.fmdn.common.livedata.SingleLiveEvent

interface ErrorEventSource {
    val errorEvents: SingleLiveEvent<Throwable>
}