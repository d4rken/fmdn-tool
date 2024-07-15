package eu.darken.fmdn.tracker.ui.list.items

import android.view.ViewGroup
import eu.darken.fmdn.R
import eu.darken.fmdn.common.asHumanReadableHex
import eu.darken.fmdn.common.lists.binding
import eu.darken.fmdn.databinding.TrackerListAfnItemBinding
import eu.darken.fmdn.tracker.core.afn.AFNTracker
import eu.darken.fmdn.tracker.ui.list.TrackerAdapter
import kotlin.math.abs

class AFNTrackerCardVH(parent: ViewGroup) :
    TrackerAdapter.BaseVH<AFNTrackerCardVH.Item, TrackerListAfnItemBinding>(
        R.layout.tracker_list_afn_item,
        parent
    ) {

    override val viewBinding = lazy { TrackerListAfnItemBinding.bind(itemView) }

    override val onBindData: TrackerListAfnItemBinding.(
        item: Item,
        payloads: List<Any>
    ) -> Unit = binding { item ->
        title.text = item.tracker.address
        subtitle.text = item.tracker.raw?.asHumanReadableHex()
        distance.text = (((100 - abs(item.tracker.rssi)) / 100f).toString())
    }

    data class Item(
        val tracker: AFNTracker,
    ) : TrackerAdapter.Item {
        override val stableId: Long = tracker.id.hashCode().toLong()
    }
}