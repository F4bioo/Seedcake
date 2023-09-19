package com.fappslab.seedcake.libraries.arch.adapter

import androidx.recyclerview.widget.DiffUtil

class BaseDiffCallback<I : Any> : DiffUtil.ItemCallback<I>() {

    override fun areItemsTheSame(oldItem: I, newItem: I): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: I, newItem: I): Boolean {
        return areItemsTheSame(oldItem, newItem)
    }
}
