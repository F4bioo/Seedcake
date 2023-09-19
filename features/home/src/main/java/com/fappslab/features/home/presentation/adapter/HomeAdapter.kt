package com.fappslab.features.home.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.fappslab.features.common.domain.model.Seed
import com.fappslab.seedcake.libraries.arch.adapter.BaseDiffCallback

internal class HomeAdapter(
    private val itemClicked: OnMainItemClicked
) : ListAdapter<Seed, HomeHolder>(BaseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeHolder =
        HomeHolder.create(parent, itemClicked)

    override fun onBindViewHolder(holder: HomeHolder, position: Int) {
        getItem(position)?.let(holder::bind)
    }
}
