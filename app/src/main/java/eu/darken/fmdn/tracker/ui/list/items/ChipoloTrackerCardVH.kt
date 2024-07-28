package eu.darken.fmdn.tracker.ui.list.items

import android.view.ViewGroup
import eu.darken.fmdn.R
import eu.darken.fmdn.common.asHex
import eu.darken.fmdn.common.lists.binding
import eu.darken.fmdn.databinding.TrackerListGfdItemBinding
import eu.darken.fmdn.databinding.TrackerListTileItemBinding
import eu.darken.fmdn.tracker.core.chipolo.ChipoloTracker
import eu.darken.fmdn.tracker.core.gfd.GFDTracker
import eu.darken.fmdn.tracker.core.tile.TileTracker
import eu.darken.fmdn.tracker.ui.list.TrackerAdapter
import kotlin.math.abs

class ChipoloTrackerCardVH(parent: ViewGroup) :
    TrackerAdapter.BaseVH<ChipoloTrackerCardVH.Item, TrackerListTileItemBinding>(
        R.layout.tracker_list_tile_item,
        parent
    ) {

    override val viewBinding = lazy { TrackerListTileItemBinding.bind(itemView) }

    override val onBindData: TrackerListTileItemBinding.(
        item: Item,
        payloads: List<Any>
    ) -> Unit = binding { item ->
        title.text = item.tracker.label.get(context)
        subtitle.text = item.tracker.lastPing?.quickInfo
        distance.text = item.tracker.lastPing?.let { (((100 - abs(it.rssi)) / 100f).toString()) }
    }

    data class Item(
        val tracker: ChipoloTracker,
    ) : TrackerAdapter.Item {
        override val stableId: Long = tracker.id.hashCode().toLong()
    }
}