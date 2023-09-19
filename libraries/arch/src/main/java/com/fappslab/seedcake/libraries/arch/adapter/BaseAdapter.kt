package com.fappslab.seedcake.libraries.arch.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

// I: Item model for adapter
// IC: ItemClicked sealed class
// VH: ViewHolder

abstract class BaseAdapter<I, IC, VH : BaseViewHolder<I>>(
    private val itemClicked: OnBaseItemClicked<IC>,
    diffCallback: DiffUtil.ItemCallback<I>,
    private val createViewHolderFunc: (ViewGroup, OnBaseItemClicked<IC>) -> VH
) : ListAdapter<I, VH>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        createViewHolderFunc(parent, itemClicked)

    override fun onBindViewHolder(holder: VH, position: Int) {
        getItem(position)?.let(holder::bind)
    }
}
