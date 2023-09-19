package com.fappslab.features.about.presentation.adapter.header

import com.fappslab.features.about.presentation.model.Header
import com.fappslab.features.about.presentation.model.OnItem
import com.fappslab.seedcake.libraries.arch.adapter.BaseAdapter
import com.fappslab.seedcake.libraries.arch.adapter.BaseDiffCallback
import com.fappslab.seedcake.libraries.arch.adapter.OnBaseItemClicked

internal class HeaderAdapter(
    onClicked: OnBaseItemClicked<OnItem>
) : BaseAdapter<Header, OnItem, HeaderHolder>(onClicked, BaseDiffCallback(), { parent, clicked ->
    HeaderHolder.create(parent, clicked)
})
