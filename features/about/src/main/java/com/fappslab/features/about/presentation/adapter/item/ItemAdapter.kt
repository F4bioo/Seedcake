package com.fappslab.features.about.presentation.adapter.item

import com.fappslab.features.about.presentation.model.Item
import com.fappslab.features.about.presentation.model.OnItem
import com.fappslab.seedcake.libraries.arch.adapter.BaseAdapter
import com.fappslab.seedcake.libraries.arch.adapter.BaseDiffCallback
import com.fappslab.seedcake.libraries.arch.adapter.OnBaseItemClicked

internal class ItemAdapter(
    onClicked: OnBaseItemClicked<OnItem>
) : BaseAdapter<Item, OnItem, ItemHolder>(onClicked, BaseDiffCallback(), ItemHolder::create)
