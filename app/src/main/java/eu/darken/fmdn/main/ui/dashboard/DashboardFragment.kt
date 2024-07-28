package eu.darken.fmdn.main.ui.dashboard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import eu.darken.fmdn.R
import eu.darken.fmdn.common.BuildConfigWrap
import eu.darken.fmdn.common.lists.differ.update
import eu.darken.fmdn.common.lists.setupDefaults
import eu.darken.fmdn.common.navigation.doNavigate
import eu.darken.fmdn.common.uix.Fragment3
import eu.darken.fmdn.common.viewbinding.viewBinding
import eu.darken.fmdn.databinding.MainFragmentBinding
import eu.darken.fmdn.tracker.ui.list.TrackerAdapter
import javax.inject.Inject

@AndroidEntryPoint
class DashboardFragment : Fragment3(R.layout.main_fragment) {

    override val vm: DashboardFragmentVM by viewModels()
    override val ui: MainFragmentBinding by viewBinding()

    @Inject lateinit var trackerAdapter: TrackerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ui.toolbar.apply {
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_settings -> {
                        doNavigate(DashboardFragmentDirections.actionDashboardToSettingsContainerFragment())
                        true
                    }

                    else -> super.onOptionsItemSelected(it)
                }
            }
            subtitle = "Buildtype: ${BuildConfigWrap.BUILD_TYPE}"
        }

        ui.list.setupDefaults(trackerAdapter, dividers = false)

        vm.listItems.observe2(ui) {
            toolbar.subtitle = resources.getQuantityString(
                R.plurals.general_tracker_in_range_count,
                it.nearbyCount,
                it.nearbyCount,
            )
            trackerAdapter.update(it.items)
        }

        vm.newRelease.observe2(ui) { release ->
            Snackbar
                .make(
                    requireView(),
                    "New release available",
                    Snackbar.LENGTH_LONG
                )
                .setAction("Show") {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(release.htmlUrl)
                    }
                    requireActivity().startActivity(intent)
                }
                .show()
        }

        super.onViewCreated(view, savedInstanceState)
    }
}
