package eu.darken.fmdn.tracker.ui.list

import android.app.Activity
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.scopes.ActivityScoped
import eu.darken.fmdn.common.lists.BindableVH
import eu.darken.fmdn.common.lists.differ.AsyncDiffer
import eu.darken.fmdn.common.lists.differ.DifferItem
import eu.darken.fmdn.common.lists.differ.HasAsyncDiffer
import eu.darken.fmdn.common.lists.differ.setupDiffer
import eu.darken.fmdn.common.lists.modular.ModularAdapter
import eu.darken.fmdn.common.lists.modular.mods.DataBinderMod
import eu.darken.fmdn.common.lists.modular.mods.TypedVHCreatorMod
import eu.darken.fmdn.sonar.core.devices.chipolo.ChipoloTrackerPing
import eu.darken.fmdn.tracker.ui.list.items.AFNTrackerCardVH
import eu.darken.fmdn.tracker.ui.list.items.ChipoloTrackerCardVH
import eu.darken.fmdn.tracker.ui.list.items.GFDTrackerCardVH
import eu.darken.fmdn.tracker.ui.list.items.SamsungTrackerCardVH
import eu.darken.fmdn.tracker.ui.list.items.TileTrackerCardVH
import javax.inject.Inject


@ActivityScoped
class TrackerAdapter @Inject constructor(
    private val activity: Activity,
) :
    ModularAdapter<TrackerAdapter.BaseVH<TrackerAdapter.Item, ViewBinding>>(),
    HasAsyncDiffer<TrackerAdapter.Item> {

    override val asyncDiffer: AsyncDiffer<*, Item> = setupDiffer()

    override fun getItemCount(): Int = data.size

    init {
        addMod(DataBinderMod(data))
        addMod(TypedVHCreatorMod({ data[it] is GFDTrackerCardVH.Item }) { GFDTrackerCardVH(it) })
        addMod(TypedVHCreatorMod({ data[it] is AFNTrackerCardVH.Item }) { AFNTrackerCardVH(it) })
        addMod(TypedVHCreatorMod({ data[it] is TileTrackerCardVH.Item }) { TileTrackerCardVH(it) })
        addMod(TypedVHCreatorMod({ data[it] is SamsungTrackerCardVH.Item }) { SamsungTrackerCardVH(it) })
        addMod(TypedVHCreatorMod({ data[it] is ChipoloTrackerCardVH.Item }) { ChipoloTrackerCardVH(it) })
    }

    abstract class BaseVH<D : Item, B : ViewBinding>(
        @LayoutRes layoutId: Int,
        parent: ViewGroup
    ) : VH(layoutId, parent), BindableVH<D, B>

    interface Item : DifferItem {
        override val payloadProvider: ((DifferItem, DifferItem) -> DifferItem?)
            get() = { old, new -> if (new::class.isInstance(old)) new else null }
    }
}