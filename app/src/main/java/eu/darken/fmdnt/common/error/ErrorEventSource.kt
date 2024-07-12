package eu.darken.fmdnt.common.error

import eu.darken.fmdnt.common.livedata.SingleLiveEvent

interface ErrorEventSource {
    val errorEvents: SingleLiveEvent<Throwable>
}