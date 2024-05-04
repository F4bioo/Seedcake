package com.fappslab.features.encrypt.seed.presentation.adapter.suggestion

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.fappslab.seedcake.libraries.arch.adapter.BaseDiffCallback

internal typealias OnSuggestionItemClicked = (String) -> Unit

internal class SuggestionAdapter(
    private val itemClicked: OnSuggestionItemClicked
) : ListAdapter<String, SuggestionHolder>(BaseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionHolder =
        SuggestionHolder.create(parent, itemClicked)

    override fun onBindViewHolder(holder: SuggestionHolder, position: Int) {
        getItem(position)?.let(holder::bind)
    }
}
