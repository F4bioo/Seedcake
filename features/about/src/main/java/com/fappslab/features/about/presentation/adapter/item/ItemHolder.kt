package com.fappslab.features.about.presentation.adapter.item

import android.view.LayoutInflater
import android.view.ViewGroup
import com.fappslab.features.about.presentation.model.Item
import com.fappslab.features.about.presentation.model.OnItem
import com.fappslab.seedcake.features.about.databinding.AboutAdapterItemBinding
import com.fappslab.seedcake.libraries.arch.adapter.BaseViewHolder
import com.fappslab.seedcake.libraries.arch.adapter.OnBaseItemClicked

internal class ItemHolder(
    private val binding: AboutAdapterItemBinding,
    private val itemClicked: OnBaseItemClicked<OnItem>
) : BaseViewHolder<Item>(binding.root) {

    override fun bind(item: Item) = binding.run {
        textItem.setText(item.textRes)
        textItem.setCompoundDrawablesWithIntrinsicBounds(item.iconRes, 0, 0, 0)
        cardContainer.setOnClickListener { itemClicked(OnItem.Item(item.externalLink)) }
    }

    companion object {
        fun create(parent: ViewGroup, itemClicked: OnBaseItemClicked<OnItem>): ItemHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = AboutAdapterItemBinding.inflate(inflater, parent, false)

            return ItemHolder(binding, itemClicked)
        }
    }
}
