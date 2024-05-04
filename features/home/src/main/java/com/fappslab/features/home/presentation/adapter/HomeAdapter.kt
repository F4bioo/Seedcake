package com.fappslab.features.home.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.fappslab.features.common.domain.model.Seed

internal class HomeAdapter(
    private val itemClicked: OnMainItemClicked
) : ListAdapter<Seed, HomeHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeHolder =
        HomeHolder.create(parent, itemClicked)

    override fun onBindViewHolder(holder: HomeHolder, position: Int) {
        getItem(position)?.let(holder::bind)
    }

    private companion object DiffCallback : DiffUtil.ItemCallback<Seed>() {
        override fun areItemsTheSame(oldItem: Seed, newItem: Seed): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Seed, newItem: Seed): Boolean {
            return oldItem == newItem
        }
    }
}
