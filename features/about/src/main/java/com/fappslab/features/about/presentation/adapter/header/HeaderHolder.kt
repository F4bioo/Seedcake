package com.fappslab.features.about.presentation.adapter.header

import android.view.LayoutInflater
import android.view.ViewGroup
import com.fappslab.features.about.presentation.model.Header
import com.fappslab.features.about.presentation.model.OnItem
import com.fappslab.seedcake.features.about.databinding.AboutAdapterHeaderBinding
import com.fappslab.seedcake.libraries.arch.adapter.BaseViewHolder
import com.fappslab.seedcake.libraries.arch.adapter.OnBaseItemClicked

internal class HeaderHolder(
    private val binding: AboutAdapterHeaderBinding,
    private val itemClicked: OnBaseItemClicked<OnItem>
) : BaseViewHolder<Header>(binding.root) {

    override fun bind(item: Header) = binding.run {
        textTitle.setText(item.titleRes)
        imageAvatar.setBackgroundResource(item.bgRes)
        imageAvatar.setImageResource(item.imageRes)
        buttonRate.setText(item.buttonTextRes)
        buttonRate.setOnClickListener { itemClicked(OnItem.Header) }
        buttonClose.setOnClickListener { itemClicked(OnItem.Close) }
    }

    companion object {
        fun create(parent: ViewGroup, itemClicked: OnBaseItemClicked<OnItem>): HeaderHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = AboutAdapterHeaderBinding.inflate(inflater, parent, false)

            return HeaderHolder(binding, itemClicked)
        }
    }
}
