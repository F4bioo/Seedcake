package com.fappslab.features.encrypt.seed.presentation.adapter.algorithm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.fappslab.features.encrypt.seed.presentation.extension.setDrawableEnd
import com.fappslab.features.encrypt.seed.presentation.extension.setTextTitle
import com.fappslab.features.encrypt.seed.presentation.model.AlgorithmItem
import com.fappslab.seedcake.features.encrypt.databinding.EncryptAlgorithmItemBinding
import com.fappslab.seedcake.libraries.arch.adapter.BaseViewHolder
import com.fappslab.seedcake.libraries.arch.adapter.OnBaseItemClicked


internal class AlgorithmHolder(
    private val binding: EncryptAlgorithmItemBinding,
    private val itemClicked: OnBaseItemClicked<AlgorithmItem.OnItem>
) : BaseViewHolder<AlgorithmItem>(binding.root) {

    override fun bind(item: AlgorithmItem) {
        binding.run {
            textTitle.setTextTitle(item.type)
            textDetailsTitle.text = item.type.cipherSpec.type
            textDetailsContent.setText(item.type.detailsContentRes)
            textSubtitle.setDrawableEnd(item.expandableIconRes)
            cardItem.setOnClickListener { itemClicked(AlgorithmItem.OnItem.Card(item)) }
            buttonUse.setOnClickListener { itemClicked(AlgorithmItem.OnItem.Use(item.type)) }
            detailsContainer.isVisible = item.isExpanded
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            itemClicked: OnBaseItemClicked<AlgorithmItem.OnItem>
        ): AlgorithmHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = EncryptAlgorithmItemBinding.inflate(inflater, parent, false)

            return AlgorithmHolder(binding, itemClicked)
        }
    }
}
