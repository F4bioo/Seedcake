package com.fappslab.seedcake.libraries.arch.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

// I: Item model for adapter
// IC: ItemClicked sealed class

typealias OnBaseItemClicked<IC> = (IC) -> Unit

abstract class BaseViewHolder<I>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(item: I)
}
