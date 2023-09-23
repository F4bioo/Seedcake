package com.fappslab.features.home.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fappslab.features.common.domain.model.Seed
import com.fappslab.seedcake.features.home.databinding.HomeAdapterItemBinding
import com.fappslab.seedcake.libraries.arch.dateformat.toDateFormatted

internal typealias OnMainItemClicked = (Seed) -> Unit

internal class HomeHolder(
    private val binding: HomeAdapterItemBinding,
    private val itemClicked: OnMainItemClicked
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(seed: Seed) = binding.run {
        textAlias.text = seed.alias
        textEncryptedSeed.text = seed.encryptedSeed
        textEncryptedDate.text = seed.date.toDateFormatted()
        cardItem.setOnClickListener { itemClicked(seed) }
    }

    companion object {
        fun create(parent: ViewGroup, onItemClicked: OnMainItemClicked): HomeHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = HomeAdapterItemBinding
                .inflate(inflater, parent, false)

            return HomeHolder(binding, onItemClicked)
        }
    }
}
