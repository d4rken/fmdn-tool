package eu.darken.fmdn.tracker.ui.list.items

import android.view.ViewGroup
import eu.darken.fmdn.R
import eu.darken.fmdn.common.asHex
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
        title.text = item.tracker.label.get(context)
        subtitle.text = item.tracker.lastPing?.quickInfo
        distance.text = item.tracker.lastPing?.let { (((100 - abs(it.rssi)) / 100f).toString()) }
    }

    data class Item(
        val tracker: AFNTracker,
    ) : TrackerAdapter.Item {
        override val stableId: Long = tracker.id.hashCode().toLong()
    }
}