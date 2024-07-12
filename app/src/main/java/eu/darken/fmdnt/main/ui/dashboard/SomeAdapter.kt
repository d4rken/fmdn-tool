package eu.darken.fmdnt.main.ui.dashboard

import android.view.ViewGroup
import eu.darken.fmdnt.R
import eu.darken.fmdnt.common.lists.BindableVH
import eu.darken.fmdnt.common.lists.differ.AsyncDiffer
import eu.darken.fmdnt.common.lists.differ.DifferItem
import eu.darken.fmdnt.common.lists.differ.HasAsyncDiffer
import eu.darken.fmdnt.common.lists.differ.setupDiffer
import eu.darken.fmdnt.common.lists.modular.ModularAdapter
import eu.darken.fmdnt.common.lists.modular.mods.DataBinderMod
import eu.darken.fmdnt.common.lists.modular.mods.SimpleVHCreatorMod
import eu.darken.fmdnt.databinding.SomeItemLineBinding
import javax.inject.Inject


class SomeAdapter @Inject constructor() : ModularAdapter<SomeAdapter.ItemVH>(),
    HasAsyncDiffer<SomeAdapter.Item> {

    override val asyncDiffer: AsyncDiffer<*, Item> = setupDiffer()

    override fun getItemCount(): Int = data.size

    init {
        addMod(DataBinderMod(data))
        addMod(SimpleVHCreatorMod { ItemVH(it) })
    }

    data class Item(
        val label: String,
        val number: Long,
        val onClickAction: (Long) -> Unit
    ) : DifferItem {
        override val stableId: Long = label.hashCode().toLong()
    }

    class ItemVH(parent: ViewGroup) : VH(R.layout.some_item_line, parent),
        BindableVH<Item, SomeItemLineBinding> {

        override val viewBinding: Lazy<SomeItemLineBinding> = lazy {
            SomeItemLineBinding.bind(itemView)
        }

        override val onBindData: SomeItemLineBinding.(
            item: Item,
            payloads: List<Any>
        ) -> Unit = { item, _ ->
            numberDisplay.text = "${item.label} #${item.number}"

            itemView.setOnClickListener { item.onClickAction(item.number) }
        }
    }
}