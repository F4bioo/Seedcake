package com.fappslab.features.encrypt.main.presentation.adapter

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import com.fappslab.seedcake.features.encrypt.R

internal class CachedSeedAdapter(
    context: Context,
    private val wordList: List<String>
) : ArrayAdapter<String>(context, R.layout.dropdown_item, wordList) {

    private val cache = mutableMapOf<String, List<String>>()
    private var filteredItems = wordList

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                val key = constraint?.toString()

                // Assign filteredItems based on the conditions
                filteredItems = when {
                    constraint.isNullOrEmpty() -> {
                        // If the constraint is null or empty, we use the whole list
                        wordList
                    }

                    key != null && cache.containsKey(key) -> {
                        // If cache already contains the results for this key, use them
                        cache[key] ?: emptyList()
                    }

                    else -> {
                        // If cache doesn't contain the results for this key, compute them and add to cache
                        wordList.filter { it.startsWith(constraint, ignoreCase = true) }
                            .also { newResults -> key?.let { cache[it] = newResults } }
                    }
                }

                // Assign the results to the FilterResults object
                filterResults.values = filteredItems
                filterResults.count = filteredItems.size

                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }
        }
    }

    override fun getCount(): Int {
        return filteredItems.size
    }

    override fun getItem(position: Int): String {
        return filteredItems[position]
    }
}
