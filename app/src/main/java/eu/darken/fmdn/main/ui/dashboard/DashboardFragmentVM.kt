package eu.darken.fmdn.main.ui.dashboard

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.darken.fmdn.common.BuildConfigWrap
import eu.darken.fmdn.common.coroutine.DispatcherProvider
import eu.darken.fmdn.common.debug.logging.Logging.Priority.ERROR
import eu.darken.fmdn.common.debug.logging.asLog
import eu.darken.fmdn.common.debug.logging.log
import eu.darken.fmdn.common.debug.logging.logTag
import eu.darken.fmdn.common.github.GithubReleaseCheck
import eu.darken.fmdn.common.uix.ViewModel3
import eu.darken.fmdn.tracker.core.TrackerHub
import eu.darken.fmdn.tracker.core.gfd.GFDTracker
import eu.darken.fmdn.tracker.ui.list.items.GFDTrackerCardVH
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import net.swiftzer.semver.SemVer
import javax.inject.Inject

@HiltViewModel
class DashboardFragmentVM @Inject constructor(
    handle: SavedStateHandle,
    dispatcherProvider: DispatcherProvider,
    githubReleaseCheck: GithubReleaseCheck,
    private val trackerHub: TrackerHub,
) : ViewModel3(dispatcherProvider = dispatcherProvider) {

    val newRelease = flow {
        val latestRelease = try {
            githubReleaseCheck.latestRelease("d4rken", "fmdn-tool")
        } catch (e: Exception) {
            log(TAG, ERROR) { "Release check failed: ${e.asLog()}" }
            null
        }
        emit(latestRelease)
    }
        .filterNotNull()
        .filter {
            val current = try {
                SemVer.parse(BuildConfigWrap.VERSION_NAME.removePrefix("v"))
            } catch (e: IllegalArgumentException) {
                log(TAG, ERROR) { "Failed to parse current version: ${e.asLog()}" }
                return@filter false
            }
            log(TAG) { "Current version is $current" }

            val latest = try {
                SemVer.parse(it.tagName.removePrefix("v")).nextMinor()
            } catch (e: IllegalArgumentException) {
                log(TAG, ERROR) { "Failed to parse current version: ${e.asLog()}" }
                return@filter false
            }
            log(TAG) { "Latest version is $latest" }
            current < latest
        }
        .asLiveData2()

    val listItems = trackerHub.devices.map { trackers ->
        trackers.mapNotNull {
            when (it) {
                is GFDTracker -> GFDTrackerCardVH.Item(
                    tracker = it
                )

                else -> null
            }
        }
    }.asLiveData2()

    companion object {
        private val TAG = logTag("Dashboard", "VM")
    }
}