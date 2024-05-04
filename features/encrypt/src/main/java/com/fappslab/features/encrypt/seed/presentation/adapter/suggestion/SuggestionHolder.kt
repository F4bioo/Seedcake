package com.fappslab.features.encrypt.seed.presentation.adapter.suggestion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fappslab.seedcake.features.encrypt.databinding.EncryptSuggestionItemBinding

internal class SuggestionHolder(
    private val binding: EncryptSuggestionItemBinding,
    private val itemClicked: OnSuggestionItemClicked
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(suggestion: String) = binding.run {
        textSuggestion.text = suggestion
        cardContainer.setOnClickListener { itemClicked(suggestion) }
    }

    companion object {
        fun create(parent: ViewGroup, onItemClicked: OnSuggestionItemClicked): SuggestionHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = EncryptSuggestionItemBinding.inflate(inflater, parent, false)

            return SuggestionHolder(binding, onItemClicked)
        }
    }
}
