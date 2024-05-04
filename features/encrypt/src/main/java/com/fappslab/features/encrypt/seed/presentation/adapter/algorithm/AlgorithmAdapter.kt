package com.fappslab.features.encrypt.seed.presentation.adapter.algorithm

import androidx.recyclerview.widget.DiffUtil
import com.fappslab.features.encrypt.seed.presentation.model.AlgorithmItem
import com.fappslab.features.encrypt.seed.presentation.model.AlgorithmItem.OnItem
import com.fappslab.seedcake.libraries.arch.adapter.BaseAdapter
import com.fappslab.seedcake.libraries.arch.adapter.OnBaseItemClicked

internal class AlgorithmAdapter(
    onClicked: OnBaseItemClicked<OnItem>
) : BaseAdapter<AlgorithmItem, OnItem, AlgorithmHolder>(
    onClicked, DiffCallback, AlgorithmHolder::create
) {

    private companion object DiffCallback : DiffUtil.ItemCallback<AlgorithmItem>() {
        override fun areItemsTheSame(oldItem: AlgorithmItem, newItem: AlgorithmItem): Boolean {
            return oldItem.type.name == newItem.type.name
        }

        override fun areContentsTheSame(oldItem: AlgorithmItem, newItem: AlgorithmItem): Boolean {
            return oldItem == newItem
        }
    }
}
